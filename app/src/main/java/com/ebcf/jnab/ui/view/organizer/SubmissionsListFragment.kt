package com.ebcf.jnab.ui.view.organizer

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentSubmissionsListBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase
import com.ebcf.jnab.ui.adapter.organizer.SubmissionAdapter
import com.ebcf.jnab.ui.viewmodel.organizer.SubmissionsListViewModel

class SubmissionsListFragment : Fragment() {

    private var _binding: FragmentSubmissionsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SubmissionAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubmissionsListBinding.inflate(inflater, container, false)

        // Crea el ViewModel sin Factory
        val submissionListViewModel = ViewModelProvider(this)[SubmissionsListViewModel::class.java]

        // Crea una instancia del caso de uso
        val formatDateUseCase = FormatDateUseCase()

        // Configura RecyclerView
        val recyclerView = binding.recyclerViewSubmissions
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observa el ViewModel para la lista de trabajos
        submissionListViewModel.submissions.observe(viewLifecycleOwner) { submissions ->
            // Se instancia el adaptador con la lista de trabajos
            adapter = SubmissionAdapter(submissions, formatDateUseCase)
            recyclerView.adapter = adapter
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
        val bottomSheet = FilterBottomSheetFragment()
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