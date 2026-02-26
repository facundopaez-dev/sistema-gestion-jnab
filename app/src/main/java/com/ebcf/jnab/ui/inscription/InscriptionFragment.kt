package com.ebcf.jnab.ui.inscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ebcf.jnab.presentation.inscripcion.InscriptionViewModel

class InscriptionFragment : Fragment() {

    private val viewModel: InscriptionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                InscriptionScreen(viewModel = viewModel)
            }
        }
    }
}
