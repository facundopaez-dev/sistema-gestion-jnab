package com.ebcf.jnab.ui.inscription


import AdminInscripcionesAdapter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.databinding.FragmentAdminInscripcionesBinding
import kotlinx.coroutines.flow.collectLatest

class AdminInscripcionesFragment : Fragment() {

    private var _binding: FragmentAdminInscripcionesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminInscripcionesViewModel by viewModels()
    private lateinit var adapter: AdminInscripcionesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminInscripcionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        adapter = AdminInscripcionesAdapter(
            onAprobar = { inscripcion -> viewModel.actualizarEstado(inscripcion.userId, "aprobado") },
            onRechazar = { inscripcion -> viewModel.actualizarEstado(inscripcion.userId, "rechazado") }
        )

        binding.recyclerInscripciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerInscripciones.adapter = adapter

        // Observar inscripciones
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.inscripciones.collectLatest { lista ->
                adapter.submitList(lista)
            }
        }

        // Observar estado de carga
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collectLatest { loading ->
                binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            }
        }

        // Observar errores
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest { msg ->
                msg?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }

        // Cargar inscripciones
        viewModel.cargarInscripciones()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}