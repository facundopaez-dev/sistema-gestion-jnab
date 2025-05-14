package com.ebcf.jnab.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ebcf.jnab.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import android.util.Patterns

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

        // Configura el click del boton de inicio de sesion
        binding.loginButton.setOnClickListener {
            val minPasswordLength = 6
            val email = binding.emailAddressField.text.toString().trim()
            val password = binding.passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    "Todos los campos deben estar completos",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Snackbar.make(
                    requireView(),
                    "Correo electrónico no válido",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (password.length < minPasswordLength) {
                Snackbar.make(
                    requireView(),
                    "La contraseña debe tener al menos $minPasswordLength caracteres",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (!result.success) {
                Snackbar.make(
                    requireView(),
                    "Correo electrónico o contraseña incorrectos",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
