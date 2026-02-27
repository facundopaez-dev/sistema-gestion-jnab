package com.ebcf.jnab.ui.speaker.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ebcf.jnab.R

class SpeakerDetailFragment : Fragment() {

    private lateinit var viewModel: SpeakerDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    SpeakerDetailScreen(
                        viewModel = viewModel,
                        onNavigateBack = { findNavController().navigateUp() },
                        onNavigateToTalkHistory = { speakerId ->
                            val bundle = Bundle().apply { putInt("speakerId", speakerId) }
                            findNavController().navigate(R.id.action_speakerDetailFragment_to_talkHistoryFragment, bundle)
                        }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val speakerId = arguments?.getInt("speakerId") ?: return

        viewModel = ViewModelProvider(this)[SpeakerDetailViewModel::class.java]
        viewModel.loadSpeakerById(speakerId)
    }
}
