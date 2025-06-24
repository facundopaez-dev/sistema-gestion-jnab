package com.ebcf.jnab.presentation.inscripcion

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class InscriptionViewModel : ViewModel() {


    data class ComprobanteInfo(
        val nombreArchivo: String,
        val estado: String
    )


    private val _comprobanteInfo = MutableStateFlow<ComprobanteInfo?>(null)
    val comprobanteInfo: StateFlow<ComprobanteInfo?> = _comprobanteInfo


    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    private val _selectedPdfUri = MutableStateFlow<Uri?>(null)
    val selectedPdfUri: StateFlow<Uri?> = _selectedPdfUri

    private val _estadoComprobante = MutableStateFlow<String?>(null)
    val estadoComprobante: StateFlow<String?> = _estadoComprobante

    sealed class UploadState {
        object Idle : UploadState()
        object Uploading : UploadState()
        object Success : UploadState()
        data class Error(val message: String) : UploadState()
    }

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState

    fun onPdfSelected(uri: Uri) {
        _selectedPdfUri.value = uri
    }

    fun uploadComprobante(context: Context, uri: Uri) {
        val user = auth.currentUser ?: run {
            _uploadState.value = UploadState.Error("Usuario no autenticado")
            return
        }


        viewModelScope.launch {
            _uploadState.value = UploadState.Uploading
            try {
                // Leer bytes del archivo
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()

                if (bytes == null) {
                    _uploadState.value = UploadState.Error("No se pudo leer el archivo")
                    return@launch
                }

                // Verificar tamaño (Firestore acepta hasta 1 MB por documento)
                if (bytes.size > 900_000) {
                    _uploadState.value = UploadState.Error("El archivo es muy grande (máximo 900 KB)")
                    return@launch
                }
                val fileName = obtenerNombreArchivo(uri, context)

                // Codificar en Base64
                val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)

                // Guardar en Firestore
                val data = mapOf(
                    "userId" to user.uid,
                    "comprobanteBase64" to base64,
                    "estado" to "pendiente",
                    "nombreArchivo" to fileName,
                    "fechaRegistro" to FieldValue.serverTimestamp()
                )

                firestore.collection("inscripciones").document(user.uid)
                    .set(data, com.google.firebase.firestore.SetOptions.merge()).await()

                _uploadState.value = UploadState.Success
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun cargarEstadoComprobante() {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            try {
                val doc = firestore.collection("inscripciones").document(user.uid).get().await()
                if (doc.exists()) {
                    val estado = doc.getString("estado")
                    _estadoComprobante.value = estado ?: "sin estado"
                } else {
                    _estadoComprobante.value = "sin comprobante"
                }
            } catch (e: Exception) {
                _estadoComprobante.value = "error"
            }
        }
    }

    fun cargarComprobanteExistente() {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            try {
                val doc = firestore.collection("inscripciones").document(user.uid).get().await()
                if (doc.exists()) {
                    val nombreArchivo = doc.getString("nombreArchivo") ?: "Comprobante subido"
                    val estado = doc.getString("estado") ?: "pendiente"
                    _comprobanteInfo.value = ComprobanteInfo(nombreArchivo, estado)
                }
            } catch (e: Exception) {
                // Manejo opcional de error
            }
        }
    }

    private fun obtenerNombreArchivo(uri: Uri, context: Context): String {
        var nombre = "Archivo.pdf"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex != -1) {
                nombre = it.getString(nameIndex)
            }
        }
        return nombre
    }
}
