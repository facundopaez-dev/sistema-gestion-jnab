package com.ebcf.jnab.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.databinding.FragmentSymposiumsListBinding
import com.ebcf.jnab.ui.view.adapter.SymposiumsListAdapter
import com.ebcf.jnab.ui.viewmodel.SymposiumsListViewModel

class SymposiumsListFragment : Fragment() {

    private var _binding: FragmentSymposiumsListBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val symposiumsListViewModel =
            ViewModelProvider(this).get(SymposiumsListViewModel::class.java)

        _binding = FragmentSymposiumsListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        val recyclerView = binding.recyclerViewSymposiums
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe ViewModel for symposiums list
        symposiumsListViewModel.symposiums.observe(viewLifecycleOwner) { symposiums ->
            recyclerView.adapter = SymposiumsListAdapter(symposiums)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}