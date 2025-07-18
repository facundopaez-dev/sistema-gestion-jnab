package com.ebcf.jnab.ui.inscription


import AdminInscripcionesAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.databinding.FragmentAdminInscripcionesBinding
import com.ebcf.jnab.domain.model.InscripcionItem
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.io.FileOutputStream

class AdminInscripcionesFragment : Fragment() {

    private var _binding: FragmentAdminInscripcionesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminInscripcionesViewModel by viewModels()
    private lateinit var adapter: AdminInscripcionesAdapter
    private var listaCompleta: List<InscripcionItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminInscripcionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdminInscripcionesAdapter(
            onAprobar = { inscripcion -> viewModel.actualizarEstado(inscripcion.userId, "aprobado") },
            onRechazar = { inscripcion -> viewModel.actualizarEstado(inscripcion.userId, "rechazado") },
            onVerComprobante = { base64 ->
                mostrarPdfConIntent(base64)
            }
        )

        binding.recyclerInscripciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerInscripciones.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.inscripciones.collectLatest { lista ->
                listaCompleta = lista
                filtrarListaYActualizar()
            }
        }

        binding.chipGroupEstado.setOnCheckedStateChangeListener { _, _ ->
            filtrarListaYActualizar()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.inscripciones.collectLatest { lista ->
                adapter.submitList(lista)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collectLatest { loading ->
                binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest { msg ->
                msg?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }

        viewModel.cargarInscripciones()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun mostrarPdfConIntent(base64: String) {
        try {
            val pdfBytes = Base64.decode(base64, Base64.DEFAULT)

            val pdfFile = File(requireContext().cacheDir, "comprobante_temp.pdf")
            FileOutputStream(pdfFile).use { it.write(pdfBytes) }

            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                pdfFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            startActivity(Intent.createChooser(intent, "Abrir comprobante con..."))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "No se pudo abrir el comprobante", Toast.LENGTH_LONG).show()
        }
    }


    private fun filtrarListaYActualizar() {
        val estadosSeleccionados = mutableSetOf<String>()

        if (binding.chipPendiente.isChecked) estadosSeleccionados.add("pendiente")
        if (binding.chipAprobado.isChecked) estadosSeleccionados.add("aprobado")
        if (binding.chipRechazado.isChecked) estadosSeleccionados.add("rechazado")

        val filtrada = if (estadosSeleccionados.isEmpty()) {
            listaCompleta
        } else {
            listaCompleta.filter { it.estado.lowercase() in estadosSeleccionados }
        }

        adapter.submitList(filtrada)
    }

}