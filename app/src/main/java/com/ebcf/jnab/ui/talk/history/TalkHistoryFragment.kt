package com.ebcf.jnab.ui.talk.history

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentTalkHistoryBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase
import com.ebcf.jnab.ui.talk.list.TalksListViewModel

class TalkHistoryFragment : Fragment() {

    private var _binding: FragmentTalkHistoryBinding? = null
    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTalkHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Crear una instancia del caso de uso
        val formatDateUseCase = FormatDateUseCase()
        val talksListViewModel = ViewModelProvider(this)[TalksListViewModel::class.java]

        val recyclerView = binding.recyclerViewTalks
        recyclerView.layoutManager = LinearLayoutManager(context)

        talksListViewModel.talks.observe(viewLifecycleOwner) { talks ->
            recyclerView.adapter = TalkHistoryAdapter(talks, formatDateUseCase)
        }
        val myToolbar = binding.toolbar
        myToolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24)
        myToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
