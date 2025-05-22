package com.ebcf.jnab.ui.symposium.user

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentSymposiumDetailBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase
import com.ebcf.jnab.ui.view.adapter.TalksListAdapter
import com.ebcf.jnab.ui.symposium.SymposiumsListViewModel
import com.ebcf.jnab.ui.viewmodel.TalksListViewModel

class SymposiumDetailFragment : Fragment() {

    private var _binding: FragmentSymposiumDetailBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSymposiumDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val args = SymposiumDetailFragmentArgs.fromBundle(requireArguments())
        val symposiumId = args.symposiumId



        // Crear el ViewModel sin Factory
        val talksListViewModel = ViewModelProvider(this).get(TalksListViewModel::class.java)

        val symposiumsListViewModel = ViewModelProvider(this).get(SymposiumsListViewModel::class.java)

        // Crear una instancia del caso de uso
        val formatDateUseCase = FormatDateUseCase()

        val symposium = symposiumsListViewModel.getSymposiumById(symposiumId)

        symposium?.let {
            binding.textViewTitle.text = it.title
            binding.textViewDescription.text = it.description
            binding.textViewDates.text = formatDateUseCase.formatRange(it.startDateTime, it.endDateTime)
        }

        // Configurar RecyclerView
        val recyclerView = binding.recyclerViewTalks
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observar ViewModel para la lista de simposios
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}