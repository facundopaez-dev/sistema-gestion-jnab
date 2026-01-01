package com.ebcf.jnab.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ebcf.jnab.R
import com.ebcf.jnab.data.repository.SignupRepositoryImpl
import com.ebcf.jnab.databinding.FragmentSignupBinding
import com.ebcf.jnab.util.FieldValidator
import com.ebcf.jnab.util.ERROR_INVALID_PASSWORD
import com.ebcf.jnab.util.ERROR_INVALID_EMAIL
import com.ebcf.jnab.util.MIN_PASSWORD_LENGTH

private const val ERROR_INVALID_FIRST_NAME = "Nombre inválido"
private const val ERROR_INVALID_LAST_NAME = "Apellido inválido"
private const val ERROR_INVALID_CONFIRM_PASSWORD = "Las contraseñas no coinciden"
private val NAME_PATTERN = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(?:[- ][A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$".toRegex()

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by viewModels {
        SignupViewModelFactory(SignupRepositoryImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        setupFocusListeners()
        setupClearErrorOnTextChange()
        setupClickListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // -------------------------------
    // Observers
    // -------------------------------
    private fun observeViewModel() {
        viewModel.signupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignupState.Loading -> binding.signupButton.isEnabled = false
                is SignupState.Result -> {
                    binding.signupButton.isEnabled = true
                    showMessage(state.message)
                }
            }
        }
    }

    // -------------------------------
    // Click listeners
    // -------------------------------
    private fun setupClickListeners() {
        binding.signupButton.setOnClickListener {
            if (validateAllFields()) {
                viewModel.signup(
                    email = binding.etEmail.text.toString().trim(),
                    password = binding.etPassword.text.toString().trim(),
                    firstName = binding.etFirstName.text.toString().trim(),
                    lastName = binding.etLastName.text.toString().trim()
                )
            }
        }

        binding.tvAlreadyHaveAccount.setOnClickListener {
            val firstName = binding.etFirstName.text.toString().trim()
            val lastName = binding.etLastName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            val anyFieldNotEmpty =
                firstName.isNotEmpty() || lastName.isNotEmpty() || email.isNotEmpty() ||
                        password.isNotEmpty() || confirmPassword.isNotEmpty()

            if (anyFieldNotEmpty) {
                AlertDialog.Builder(requireContext())
                    .setTitle("¿Desea salir del registro?")
                    .setMessage("Se perderán los datos ingresados. ¿Está seguro de que desea salir?")
                    .setPositiveButton("Sí") { _, _ ->
                        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                    }
                    .setNegativeButton("No", null)
                    .show()
            } else {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
        }
    }

    // -------------------------------
    // Mensajes
    // -------------------------------
    private fun showMessage(message: SignupMessage) {
        when (message) {
            SignupMessage.SignupSuccess -> {
                Snackbar.make(
                    binding.root,
                    "Registro exitoso. Revise su correo para verificar su cuenta.",
                    Snackbar.LENGTH_LONG
                ).show()

                findNavController()
                    .navigate(R.id.action_signupFragment_to_loginFragment)
            }

            SignupMessage.SignupError -> {
                Snackbar.make(
                    binding.root,
                    "Error al registrar usuario. Por favor, intenta nuevamente.",
                    Snackbar.LENGTH_LONG
                ).show()
            }

            SignupMessage.EmailAlreadyRegistered -> {
                binding.tilEmail.error = "Correo electrónico ya registrado"
            }
        }
    }

    // -------------------------------
    // Validaciones
    // -------------------------------
    private fun validateAllFields(): Boolean {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        val isFirstNameValid = FieldValidator.validateField(
            NAME_PATTERN.matches(firstName), binding.tilFirstName, ERROR_INVALID_FIRST_NAME
        )

        val isLastNameValid = FieldValidator.validateField(
            NAME_PATTERN.matches(lastName), binding.tilLastName, ERROR_INVALID_LAST_NAME
        )

        val isEmailValid = FieldValidator.validateField(
            Patterns.EMAIL_ADDRESS.matcher(email).matches(), binding.tilEmail, ERROR_INVALID_EMAIL
        )

        val isPasswordValid = FieldValidator.validateField(
            password.length >= MIN_PASSWORD_LENGTH, binding.tilPassword, ERROR_INVALID_PASSWORD
        )

        val isConfirmPasswordValid = FieldValidator.validateField(
            password == confirmPassword, binding.tilConfirmPassword, ERROR_INVALID_CONFIRM_PASSWORD
        )

        return isFirstNameValid && isLastNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
    }

    // -------------------------------
    // Listeners de foco
    // -------------------------------
    private fun setupFocusListeners() {
        binding.etFirstName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val name = binding.etFirstName.text.toString().trim()
                FieldValidator.validateField(
                    NAME_PATTERN.matches(name), binding.tilFirstName, ERROR_INVALID_FIRST_NAME
                )
            }
        }

        binding.etLastName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val name = binding.etLastName.text.toString().trim()
                FieldValidator.validateField(
                    NAME_PATTERN.matches(name), binding.tilLastName, ERROR_INVALID_LAST_NAME
                )
            }
        }

        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.etEmail.text.toString().trim()
                FieldValidator.validateField(
                    Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                    binding.tilEmail,
                    ERROR_INVALID_EMAIL
                )
            }
        }

        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = binding.etPassword.text.toString()
                FieldValidator.validateField(
                    password.length >= MIN_PASSWORD_LENGTH,
                    binding.tilPassword,
                    ERROR_INVALID_PASSWORD
                )
            }
        }

        binding.etConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = binding.etPassword.text.toString()
                val confirmPassword = binding.etConfirmPassword.text.toString()
                FieldValidator.validateField(
                    password == confirmPassword,
                    binding.tilConfirmPassword,
                    ERROR_INVALID_CONFIRM_PASSWORD
                )
            }
        }
    }

    // -------------------------------
    // Borrar errores al escribir
    // -------------------------------
    private fun setupClearErrorOnTextChange() {
        binding.etFirstName.addTextChangedListener {
            binding.tilFirstName.error = null
            binding.tilFirstName.isErrorEnabled = false
        }

        binding.etLastName.addTextChangedListener {
            binding.tilLastName.error = null
            binding.tilLastName.isErrorEnabled = false
        }

        binding.etEmail.addTextChangedListener {
            binding.tilEmail.error = null
            binding.tilEmail.isErrorEnabled = false
        }

        binding.etPassword.addTextChangedListener {
            binding.tilPassword.error = null
            binding.tilPassword.isErrorEnabled = false

            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    binding.tilConfirmPassword.error = null
                    binding.tilConfirmPassword.isErrorEnabled = false
                } else {
                    binding.tilConfirmPassword.error = ERROR_INVALID_CONFIRM_PASSWORD
                }
            }
        }

        binding.etConfirmPassword.addTextChangedListener {
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    binding.tilConfirmPassword.error = null
                    binding.tilConfirmPassword.isErrorEnabled = false
                } else {
                    binding.tilConfirmPassword.error = ERROR_INVALID_CONFIRM_PASSWORD
                }
            } else {
                binding.tilConfirmPassword.error = null
            }
        }
    }
}