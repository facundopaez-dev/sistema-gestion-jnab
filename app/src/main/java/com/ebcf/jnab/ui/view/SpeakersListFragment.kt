package com.ebcf.jnab.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.databinding.FragmentSpeakersListBinding
import com.ebcf.jnab.ui.view.adapter.SpeakersListAdapter
import com.ebcf.jnab.ui.viewmodel.SpeakersListViewModel

class SpeakersListFragment : Fragment() {

    private var _binding: FragmentSpeakersListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpeakersListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val speakersListViewModel = ViewModelProvider(this).get(SpeakersListViewModel::class.java)

        val recyclerView = binding.recyclerViewSpeakers
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observar ViewModel para la lista de expositores
        speakersListViewModel.speakers.observe(viewLifecycleOwner) { speakers ->
            recyclerView.adapter = SpeakersListAdapter(speakers)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
