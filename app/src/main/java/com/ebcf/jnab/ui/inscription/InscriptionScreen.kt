package com.ebcf.jnab.ui.inscription

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ebcf.jnab.presentation.inscripcion.InscriptionViewModel

@Composable
fun InscriptionScreen(
    viewModel: InscriptionViewModel
) {
    val context = LocalContext.current
    val uploadState by viewModel.uploadState.collectAsState()
    val comprobanteInfo by viewModel.comprobanteInfo.collectAsState()
    val selectedPdfUri by viewModel.selectedPdfUri.collectAsState()
    val estadoComprobante by viewModel.estadoComprobante.collectAsState()


    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onPdfSelected(it) }
    }

    LaunchedEffect(Unit) {
        viewModel.cargarComprobanteExistente()
        viewModel.cargarEstadoComprobante()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val isComprobanteSubido = comprobanteInfo != null

        val fileName = comprobanteInfo?.nombreArchivo
            ?: selectedPdfUri?.let { obtenerNombreArchivo(context, it) }
            ?: "Ningún archivo seleccionado"
        Text(text = fileName)

        Spacer(modifier = Modifier.height(8.dp))

        if (estadoComprobante != null || comprobanteInfo != null) {
            val estado = (comprobanteInfo?.estado ?: estadoComprobante)?.lowercase()
            val statusColor = when (estado) {
                "aprobado" -> Color(0xFF669900)
                "desaprobado" -> Color(0xFFCC0000)
                "pendiente" -> Color(0xFFFF8800)
                else -> Color.Black
            }
            Text(text = "Estado: ${comprobanteInfo?.estado ?: estadoComprobante ?: "desconocido"}", color = statusColor)
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { pdfPickerLauncher.launch("application/pdf") },
            enabled = !isComprobanteSubido
        ) {
            Text("Seleccionar PDF")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                selectedPdfUri?.let {
                    viewModel.uploadComprobante(context, it)
                }
            },
            enabled = selectedPdfUri != null && !isComprobanteSubido
        ) {
            Text("Subir Comprobante")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uploadState) {
            is InscriptionViewModel.UploadState.Uploading -> {
                CircularProgressIndicator()
            }
            is InscriptionViewModel.UploadState.Error -> {
                Text(text = "Error: ${state.message}", color = Color.Red)
            }
            else -> {}
        }
    }
}

private fun obtenerNombreArchivo(context: Context, uri: Uri): String {
    var nombre = "Archivo seleccionado"
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nombreIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst() && nombreIndex != -1) {
            nombre = cursor.getString(nombreIndex)
        }
    }
    return nombre
}
