package com.ebcf.jnab.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ebcf.jnab.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]

        // Configura el click del boton de inicio de sesion
        binding.loginButton.setOnClickListener {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
            val passwordRegex = "^.{6,}$".toRegex()
            val email = binding.emailAddressField.text.toString().trim()
            val password = binding.passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Todos los campos deben estar completos",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            if (!email.matches(emailRegex)) {
                Toast.makeText(requireContext(), "Correo electrónico no válido", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (!password.matches(passwordRegex)) {
                Toast.makeText(
                    requireContext(),
                    "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        // Observa los cambios en el LiveData
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { result ->
            if (result.success) {
                Toast.makeText(context, "Inicio de sesión satisfactorio", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    context,
                    "Correo electrónico o contraseña incorrectos",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
