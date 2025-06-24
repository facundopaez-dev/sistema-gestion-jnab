package com.ebcf.jnab.ui.inscription


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebcf.jnab.data.source.remote.FirebaseFirestoreProvider
import com.ebcf.jnab.domain.model.InscripcionItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Inscripcion(
    val userId: String = "",
    val estado: String = "",
    val fechaRegistro: com.google.firebase.Timestamp? = null
)

class AdminInscripcionesViewModel : ViewModel() {

    private val firestore = FirebaseFirestoreProvider.provide()

    private val _inscripciones = MutableStateFlow<List<InscripcionItem>>(emptyList())
    val inscripciones: StateFlow<List<InscripcionItem>> = _inscripciones

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun clearError() {
        _error.value = null
    }

    fun cargarInscripciones() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = firestore.collection("inscripciones").get().await()
                val lista = snapshot.documents.mapNotNull { doc ->
                    val userId = doc.getString("userId") ?: return@mapNotNull null
                    val estado = doc.getString("estado") ?: "pendiente"
                    val fecha = doc.getTimestamp("fechaRegistro")?.toDate()?.toString()
                    val base64 = doc.getString("comprobanteBase64") ?: return@mapNotNull null

                    InscripcionItem(
                        userId = userId,
                        estado = estado,
                        fechaRegistro = fecha,
                        comprobanteBase64 = base64
                    )
                }
                _inscripciones.value = lista
            } catch (e: Exception) {
                _error.value = "Error al cargar inscripciones: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarEstado(userId: String, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                firestore.collection("inscripciones").document(userId)
                    .update("estado", nuevoEstado).await()

                // Actualizar localmente el estado
                _inscripciones.value = _inscripciones.value.map {
                    if (it.userId == userId) it.copy(estado = nuevoEstado) else it
                }
            } catch (e: Exception) {
                _error.value = "Error al actualizar estado: ${e.message}"
            }
        }
    }

    fun toggleExpand(userId: String) {
        _inscripciones.value = _inscripciones.value.map {
            if (it.userId == userId) it.copy(expanded = !it.expanded) else it
        }
    }
}