package com.ebcf.jnab.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ebcf.jnab.R
import com.ebcf.jnab.domain.model.AuthError

private const val INVALID_CREDENTIALS =
    "Correo electrónico o contraseña incorrectos"
private const val ERROR_GENERIC =
    "Error al iniciar sesión. Por favor, inténtelo más tarde."

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                LoginScreen(
                    viewModel = viewModel,
                    onSignupClick = {
                        findNavController()
                            .navigate(R.id.action_loginFragment_to_signupFragment)
                    },
                    onForgotPasswordClick = {
                        findNavController()
                            .navigate(R.id.action_loginFragment_to_passwordRecoveryFragment)
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginError.observe(viewLifecycleOwner) { error ->
            val message = when (error) {
                AuthError.InvalidCredentials -> INVALID_CREDENTIALS
                AuthError.GenericError -> ERROR_GENERIC
            }

            Snackbar
                .make(requireView(), message, Snackbar.LENGTH_LONG)
                .show()
        }
    }
}