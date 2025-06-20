package com.ebcf.jnab.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Patterns
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentLoginBinding
import com.ebcf.jnab.util.FieldValidator
import com.ebcf.jnab.util.ERROR_INVALID_PASSWORD
import com.ebcf.jnab.util.ERROR_INVALID_EMAIL
import com.ebcf.jnab.util.MIN_PASSWORD_LENGTH

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

        setupFocusListeners()
        setupClearErrorOnTextChange()

        // Configura el click del boton de inicio de sesion
        binding.loginButton.setOnClickListener {
            val email = binding.emailAddressField.text.toString().trim()
            val password = binding.passwordField.text.toString().trim()

            if (validateAllFields()) {
                viewModel.login(email, password)
            }

        }

        viewModel.loginError.observe(viewLifecycleOwner) { errorMessage ->
            Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
        }

        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.forgotPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_passwordRecoveryFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Valida todos los campos del formulario de inicio de sesion.
     *
     * Esta funcion verifica que cada campo cumpla con sus criterios de validacion específicos,
     * mostrando mensajes de error correspondientes en los TextInputLayout asociados.
     *
     * @return `true` si todos los campos son validos, `false` si alguno no cumple la validacion.
     */
    private fun validateAllFields(): Boolean {
        val email = binding.emailAddressField.text.toString().trim()
        val password = binding.passwordField.text.toString()

        val isEmailValid = FieldValidator.validateField(
            Patterns.EMAIL_ADDRESS.matcher(email).matches(),
            binding.emailInputLayout,
            ERROR_INVALID_EMAIL
        )

        val isPasswordValid = FieldValidator.validateField(
            password.length >= MIN_PASSWORD_LENGTH,
            binding.passwordInputLayout,
            ERROR_INVALID_PASSWORD
        )

        return isEmailValid && isPasswordValid
    }

    /**
     * Configura listeners para los cambios de foco en los campos de entrada.
     *
     * Cada vez que un campo pierde el foco (cuando el usuario termina de editarlo),
     * se valida el contenido actual y, si es invalido, se muestra un mensaje de error
     * en el `TextInputLayout` correspondiente.
     *
     * Esto permite una validacion en tiempo real que guía al usuario mientras completa
     * el formulario.
     */
    private fun setupFocusListeners() {
        binding.emailAddressField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.emailAddressField.text.toString().trim()
                FieldValidator.validateField(
                    Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                    binding.emailInputLayout,
                    ERROR_INVALID_EMAIL
                )
            }
        }

        binding.passwordField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = binding.passwordField.text.toString()
                FieldValidator.validateField(
                    password.length >= MIN_PASSWORD_LENGTH,
                    binding.passwordInputLayout,
                    ERROR_INVALID_PASSWORD
                )
            }
        }
    }

    /**
     * Configura listeners para que, cuando el usuario modifique el texto en cualquiera
     * de los campos de entrada, se borre el mensaje de error correspondiente en el
     * `TextInputLayout` asociado.
     *
     * Esto mejora la experiencia de usuario al eliminar los errores visibles en cuanto
     * el usuario comienza a corregir el campo.
     */
    private fun setupClearErrorOnTextChange() {
        binding.emailAddressField.addTextChangedListener {
            binding.emailInputLayout.error = null
            binding.emailInputLayout.isErrorEnabled = false
        }

        binding.passwordField.addTextChangedListener {
            binding.passwordInputLayout.error = null
            binding.passwordInputLayout.isErrorEnabled = false
        }
    }

}
