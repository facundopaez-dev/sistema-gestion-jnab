package com.ebcf.jnab.ui.symposium.user

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.data.repository.SymposiumRepositoryImpl
import com.ebcf.jnab.data.source.local.DatabaseProvider
import com.ebcf.jnab.data.source.remote.FirebaseFirestoreProvider
import com.ebcf.jnab.data.source.remote.FirestoreSymposiumDataSource
import com.ebcf.jnab.databinding.FragmentSymposiumsListBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase
import com.ebcf.jnab.ui.symposium.SymposiumsListViewModel

class SymposiumsListFragment : Fragment() {

    private var _binding: FragmentSymposiumsListBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSymposiumsListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dao = DatabaseProvider.getDatabase(requireContext()).symposiumDao()
        val remoteDataSource = FirestoreSymposiumDataSource(FirebaseFirestoreProvider.provide())
        val repository = SymposiumRepositoryImpl(dao, remoteDataSource)
        val symposiumsListViewModel = SymposiumsListViewModel(repository)

        // Crear una instancia del caso de uso
        val formatDateUseCase = FormatDateUseCase()

        // Configurar RecyclerView
        val recyclerView = binding.recyclerViewSymposiums
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observar ViewModel para la lista de simposios
        symposiumsListViewModel.symposiums.observe(viewLifecycleOwner) { symposiums ->
            recyclerView.adapter = SymposiumsListAdapter(symposiums, formatDateUseCase) { symposium ->
                val action =
                    SymposiumsListFragmentDirections.actionNavigationSymposiumsToSymposiumDetailFragment(
                        symposium.id
                    )
                findNavController().navigate(action)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}