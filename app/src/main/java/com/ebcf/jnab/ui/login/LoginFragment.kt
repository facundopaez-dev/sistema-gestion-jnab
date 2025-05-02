package com.ebcf.jnab.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ebcf.jnab.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val email = binding.emailAddressField.text.toString().trim()
            val password = binding.passswordField.text.toString().trim()

            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
            val passwordRegex = "^.{6,}$".toRegex()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos deben estar completos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!email.matches(emailRegex)) {
                Toast.makeText(requireContext(), "Correo electrónico no válido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!password.matches(passwordRegex)) {
                Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            loginViewModel.loginUser(email, password)
        }

        // Se observa el resultado del login (ya está en el ViewModel)
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is LoginViewModel.Result.Success -> {
                    Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_LONG).show()
                    // El resultado de éxito lo recibe la actividad para hacer la navegación
                }
                is LoginViewModel.Result.Error -> {
                    // Se maneja el error en la UI del fragmento
                    Toast.makeText(requireContext(), "Correo electrónico o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
