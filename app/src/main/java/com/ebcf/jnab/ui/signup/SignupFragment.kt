package com.ebcf.jnab.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ebcf.jnab.R
import com.ebcf.jnab.data.repository.SignupRepositoryImpl

class SignupFragment : Fragment() {

    private val viewModel: SignupViewModel by viewModels {
        SignupViewModelFactory(SignupRepositoryImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    SignupScreen(
                        viewModel = viewModel,
                        onNavigateToLogin = {
                            findNavController()
                                .navigate(R.id.action_signupFragment_to_loginFragment)
                        }
                    )
                }
            }
        }
    }
}