package com.ebcf.jnab.ui.symposium.organizer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.data.repository.SymposiumRepositoryImpl
import com.ebcf.jnab.data.source.local.DatabaseProvider
import com.ebcf.jnab.data.source.remote.FirebaseFirestoreProvider
import com.ebcf.jnab.data.source.remote.FirestoreSymposiumDataSource
import com.ebcf.jnab.databinding.FragmentOrgSymposiumsListBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase
import com.ebcf.jnab.ui.symposium.SymposiumsListViewModel

class OrgSymposiumsListFragment : Fragment() {

    private var _binding: FragmentOrgSymposiumsListBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrgSymposiumsListBinding.inflate(inflater, container, false)

        val dao = DatabaseProvider.getDatabase(requireContext()).symposiumDao()
        val remoteDataSource = FirestoreSymposiumDataSource(FirebaseFirestoreProvider.provide())
        val repository = SymposiumRepositoryImpl(dao, remoteDataSource)
        val symposiumsListViewModel = SymposiumsListViewModel(repository)

        // Crea una instancia del caso de uso
        val formatDateUseCase = FormatDateUseCase()

        // Configura RecyclerView
        val recyclerView = binding.recyclerViewSymposiums
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observa ViewModel para la lista de simposios
        symposiumsListViewModel.symposiums.observe(viewLifecycleOwner) { symposiums ->
            recyclerView.adapter = OrganizerSymposiumAdapter(symposiums, formatDateUseCase)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}