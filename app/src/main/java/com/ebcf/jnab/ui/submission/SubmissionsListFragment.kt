package com.ebcf.jnab.ui.submission

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentSubmissionsListBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase

class SubmissionsListFragment : Fragment() {

    private var _binding: FragmentSubmissionsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SubmissionsListAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubmissionsListBinding.inflate(inflater, container, false)

        // Crea el ViewModel
        val submissionListViewModel = ViewModelProvider(this)[SubmissionsListViewModel::class.java]

        // Caso de uso para formatear fecha
        val formatDateUseCase = FormatDateUseCase()

        // Configura el RecyclerView con layout manager y adapter inicial vacío
        val recyclerView = binding.recyclerViewSubmissions
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Crea el adapter una sola vez, con callback para click
        adapter = SubmissionsListAdapter(formatDateUseCase) { submission ->
            val action = SubmissionsListFragmentDirections
                .actionSubmissionsListFragmentToSubmissionDetailFragment(submission.id)
            findNavController().navigate(action)
        }

        recyclerView.adapter = adapter

        // Recolecta el flow para actualizar la lista en tiempo real
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                submissionListViewModel.submissionsFlow.collect { submissions ->
                    adapter.setAllSubmissions(submissions)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura la Toolbar con ViewBinding
        val toolbar = binding.toolbarSubmissions
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Configura el menu con MenuHost (forma moderna)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_submissions, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_filter -> {
                        showFilterBottomSheet()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun showFilterBottomSheet() {
        val bottomSheet = SubmissionsFilterBottomSheetFragment()
        bottomSheet.onFilterSelected = { selectedItem ->
            // Si el item seleccionado es "Todos", se pasa null para mostrar todos los trabajos
            if (selectedItem == null) {
                adapter.filterByStatus(null)
            } else {
                adapter.filterByStatus(selectedItem)
            }
        }

        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}