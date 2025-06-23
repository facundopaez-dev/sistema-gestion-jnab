package com.ebcf.jnab.ui.talk.favourites

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.databinding.FragmentFavouriteTalksBinding
import com.ebcf.jnab.ui.talk.list.GroupedTalksAdapter
import com.ebcf.jnab.ui.talk.list.TalksListViewModel

class FavouriteTalksFragment : Fragment() {

    private var _binding: FragmentFavouriteTalksBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteTalksBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel = ViewModelProvider(
            requireActivity(), TalksListViewModel.TalksListViewModelFactory(requireContext())
        )[TalksListViewModel::class.java]

        val recyclerView = binding.recyclerViewFavouriteTalks
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = GroupedTalksAdapter()
        recyclerView.adapter = adapter

        viewModel.groupedFavorites.observe(viewLifecycleOwner) { grouped ->
            adapter.submitGroupedTalks(grouped)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

