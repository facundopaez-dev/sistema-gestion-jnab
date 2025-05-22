package com.ebcf.jnab.ui.talk

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentTalksListBinding


class TalksListFragment : Fragment() {

    private var _binding: FragmentTalksListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TalksListAdapter
    private lateinit var talksListViewModel: TalksListViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTalksListBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //scope de activity para que el
        talksListViewModel =
            ViewModelProvider(requireActivity()).get(TalksListViewModel::class.java)

        val recyclerView = binding.recyclerViewTalks
        recyclerView.layoutManager = LinearLayoutManager(context)

        talksListViewModel.favoriteIds.observe(viewLifecycleOwner) { favoriteIds ->
            val talks = talksListViewModel.talks.value ?: emptyList()
            recyclerView.adapter = TalksListAdapter(talks, favoriteIds) { talkId ->
                talksListViewModel.toggleFavorite(talkId)
            }
        }


        talksListViewModel.filteredTalks.observe(viewLifecycleOwner) { talks ->
            val favoriteIds = talksListViewModel.favoriteIds.value ?: setOf()
            recyclerView.adapter = TalksListAdapter(talks, favoriteIds) { talkId ->
                talksListViewModel.toggleFavorite(talkId)
            }
        }

        val myToolbar = binding.toolbar
//        myToolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24)
//
//        myToolbar.setNavigationOnClickListener { view ->
//            findNavController().navigateUp()
//        }

        val filterBottomSheet = TalksFilterBottomSheet()

        myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_filter -> {
                    filterBottomSheet.show(parentFragmentManager, TalksFilterBottomSheet.TAG)
                    true
                }
                else -> false
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroyView() {
        super.onDestroyView()
        if (isRemoving) {
            talksListViewModel.clearFilters() // Limpia los filtros al salir del fragmento
        }
        _binding = null
    }


}