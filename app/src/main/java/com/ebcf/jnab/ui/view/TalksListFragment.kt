package com.ebcf.jnab.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.databinding.FragmentTalksListBinding
import com.ebcf.jnab.ui.view.adapter.TalksListAdapter
import com.ebcf.jnab.ui.viewmodel.TalksListViewModel

class TalksListFragment : Fragment() {

    private var _binding: FragmentTalksListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTalksListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val talksListViewModel = ViewModelProvider(this).get(TalksListViewModel::class)

        val recyclerView = binding.recyclerViewTalks
        recyclerView.layoutManager = LinearLayoutManager(context)

        talksListViewModel.talks.observe(viewLifecycleOwner) { talks ->
            recyclerView.adapter = TalksListAdapter(talks)
        }

        return root
    }
}