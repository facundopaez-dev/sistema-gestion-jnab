package com.ebcf.jnab.ui.inscription

import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.ebcf.jnab.databinding.FragmentInscriptionBinding
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ebcf.jnab.presentation.inscripcion.InscriptionViewModel
import kotlinx.coroutines.flow.collectLatest

class InscriptionFragment : Fragment() {

    private var _binding: FragmentInscriptionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InscriptionViewModel by viewModels()

    companion object {
        private const val REQUEST_CODE_PDF = 1001
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInscriptionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cargarComprobanteExistente()

        binding.btnSeleccionarPdf.setOnClickListener {
            pickPdfFile()
        }

        binding.btnSubirComprobante.setOnClickListener {
            viewModel.selectedPdfUri.value?.let { uri ->
                viewModel.uploadComprobante(requireContext(),uri)
            } ?: Toast.makeText(requireContext(), "Por favor selecciona un PDF primero", Toast.LENGTH_SHORT).show()
        }

        // Observers
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectedPdfUri.collectLatest { uri ->
                val nombre = uri?.let { obtenerNombreArchivo(it) } ?: "Ningún archivo seleccionado"
                binding.tvPdfNombre.text = nombre            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.comprobanteInfo.collectLatest { info ->
                if (info != null) {
                    binding.tvPdfNombre.text = info.nombreArchivo

                    // Desactivamos los botones si ya subió
                    binding.btnSeleccionarPdf.isEnabled = false
                    binding.btnSubirComprobante.isEnabled = false

                    // Cambiamos color según estado
                    when (info.estado.lowercase()) {
                        "aprobado" -> {
                            binding.tvEstadoComprobante.setTextColor(
                                resources.getColor(android.R.color.holo_green_dark, null)
                            )
                        }
                        "desaprobado" -> {
                            binding.tvEstadoComprobante.setTextColor(
                                resources.getColor(android.R.color.holo_red_dark, null)
                            )
                        }
                        "pendiente" -> {
                            binding.tvEstadoComprobante.setTextColor(
                                resources.getColor(android.R.color.holo_orange_dark, null)
                            )
                        }
                    }

                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uploadState.collectLatest { state ->
                when (state) {
                    is InscriptionViewModel.UploadState.Idle -> binding.progressBar.visibility = View.GONE
                    is InscriptionViewModel.UploadState.Uploading -> binding.progressBar.visibility = View.VISIBLE
                    is InscriptionViewModel.UploadState.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is InscriptionViewModel.UploadState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_LONG).show()
                    }

                    is InscriptionViewModel.UploadState.Error -> TODO()
                    InscriptionViewModel.UploadState.Idle -> TODO()
                    InscriptionViewModel.UploadState.Success -> TODO()
                    InscriptionViewModel.UploadState.Uploading -> TODO()
                }
            }
        }

        viewModel.cargarEstadoComprobante()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.estadoComprobante.collectLatest { estado ->
                binding.tvEstadoComprobante.text = "Estado: ${estado ?: "desconocido"}"
            }
        }
    }

    private fun pickPdfFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
        }
        startActivityForResult(Intent.createChooser(intent, "Selecciona un PDF"), REQUEST_CODE_PDF)
    }

    private fun obtenerNombreArchivo(uri: Uri): String {
        var nombre = "Archivo seleccionado"
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nombreIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nombreIndex != -1) {
                nombre = it.getString(nombreIndex)
            }
        }
        return nombre
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PDF && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                viewModel.onPdfSelected(uri)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}