package com.ebcf.jnab.ui.view
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ebcf.jnab.databinding.FragmentSpeakerDetailBinding
import com.ebcf.jnab.ui.viewmodel.SpeakerDetailViewModel
import com.ebcf.jnab.R
import androidx.navigation.fragment.findNavController

class SpeakerDetailFragment : Fragment() {

    private var _binding: FragmentSpeakerDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SpeakerDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpeakerDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val speakerId = arguments?.getInt("speakerId") ?: return

        viewModel = ViewModelProvider(this)[SpeakerDetailViewModel::class.java]
        viewModel.loadSpeakerById(speakerId)

        viewModel.speaker.observe(viewLifecycleOwner) { speaker ->
            binding.textName.text = "${speaker.firstName} ${speaker.lastName}"
            binding.textInstitution.text = speaker.institution
            binding.textEmail.text = speaker.email
        }

        val myToolbar = binding.toolbar
        myToolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24)
        myToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnTalkHistory.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("speakerId", speakerId)
            }
            findNavController().navigate(R.id.action_speakerDetailFragment_to_talkHistoryFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
