package com.ebcf.jnab.ui.passwordRecovery

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ebcf.jnab.R
import com.ebcf.jnab.data.repository.AuthRepositoryImpl
import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource
import com.ebcf.jnab.databinding.FragmentPasswordRecoveryBinding
import com.ebcf.jnab.domain.repository.AuthRepository
import com.ebcf.jnab.util.ERROR_INVALID_EMAIL
import com.google.android.material.snackbar.Snackbar

class PasswordRecoveryFragment : Fragment() {

    private var _binding: FragmentPasswordRecoveryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PasswordRecoveryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordRecoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val remoteDataSource = FirebaseAuthRemoteDataSource.getInstance()
        val authRepository: AuthRepository = AuthRepositoryImpl(remoteDataSource)
        viewModel = PasswordRecoveryViewModel(authRepository)

        setupObservers()
        setupClearErrorOnTextChange()

        binding.sendButton.setOnClickListener {
            if (!validateEmailField()) {
                return@setOnClickListener
            }

            val email = binding.emailField.text?.toString()?.trim() ?: ""
            viewModel.sendPasswordResetEmail(email)
        }

        binding.backToLoginText.setOnClickListener {
            findNavController().navigate(R.id.action_passwordRecoveryFragment_to_loginFragment)
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is PasswordRecoveryViewModel.PasswordRecoveryState.Loading -> {
                    binding.sendButton.isEnabled = false
                }

                is PasswordRecoveryViewModel.PasswordRecoveryState.Success -> {
                    binding.sendButton.isEnabled = true
                    Snackbar.make(
                        binding.root,
                        "Correo enviado para restablecer la contraseña",
                        Snackbar.LENGTH_LONG
                    ).show()

                    findNavController().navigate(R.id.action_passwordRecoveryFragment_to_loginFragment)
                }

                is PasswordRecoveryViewModel.PasswordRecoveryState.Error -> {
                    binding.sendButton.isEnabled = true

                    Log.e("PasswordRecovery", "Error enviando correo", state.exception)

                    Snackbar.make(
                        binding.root,
                        "No se pudo enviar el correo. Intente nuevamente.",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Reintentar") {
                        val email = binding.emailField.text?.toString()?.trim() ?: ""
                        viewModel.sendPasswordResetEmail(email)
                    }.show()
                }
            }
        })
    }

    private fun setupClearErrorOnTextChange() {
        binding.emailField.addTextChangedListener {
            binding.emailInputLayout.error = null
            binding.emailInputLayout.isErrorEnabled = false
        }
    }

    private fun validateEmailField(): Boolean {
        val email = binding.emailField.text?.toString()?.trim() ?: ""

        if (!isValidEmail(email)) {
            binding.emailInputLayout.error = ERROR_INVALID_EMAIL
            return false
        }

        binding.emailInputLayout.error = null
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}