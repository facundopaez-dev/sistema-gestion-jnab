package com.ebcf.jnab.ui.symposiums

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.databinding.FragmentSymposiumsBinding

class Symposiums : Fragment() {

    private var _binding: FragmentSymposiumsBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val symposiumsViewModel =
            ViewModelProvider(this).get(SymposiumsViewModel::class.java)

        _binding = FragmentSymposiumsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        val recyclerView = binding.recyclerViewSymposiums
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe ViewModel for symposiums list
        symposiumsViewModel.symposiums.observe(viewLifecycleOwner) { symposiums ->
            recyclerView.adapter = SymposiumsAdapter(symposiums)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}