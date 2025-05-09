package com.ebcf.jnab.ui.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentTalksListBinding
import com.ebcf.jnab.ui.view.adapter.TalksListAdapter
import com.ebcf.jnab.ui.viewmodel.TalksListViewModel

class TalksListFragment : Fragment() {

    private var _binding: FragmentTalksListBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTalksListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val talksListViewModel = ViewModelProvider(this).get(TalksListViewModel::class)

        val recyclerView = binding.recyclerViewTalks
        recyclerView.layoutManager = LinearLayoutManager(context)

        talksListViewModel.favoriteIds.observe(viewLifecycleOwner) { favoriteIds ->
            val talks = talksListViewModel.talks.value ?: emptyList()
            recyclerView.adapter = TalksListAdapter(talks, favoriteIds) { talkId ->
                talksListViewModel.toggleFavorite(talkId)
            }
        }


        talksListViewModel.talks.observe(viewLifecycleOwner) { talks ->
            val favoriteIds = talksListViewModel.favoriteIds.value ?: setOf()
            recyclerView.adapter = TalksListAdapter(talks, favoriteIds) { talkId ->
                talksListViewModel.toggleFavorite(talkId)
            }
        }

        val myToolbar = binding.toolbar
        myToolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24)

        myToolbar.setNavigationOnClickListener { view ->
            findNavController().navigateUp()
        }


        return root
    }
}