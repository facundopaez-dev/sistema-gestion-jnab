package com.ebcf.jnab.ui.signup

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.ebcf.jnab.R
import com.ebcf.jnab.data.model.UserRole
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        setupFocusListeners()
        setupClearErrorOnTextChange()

        binding.signupButton.setOnClickListener {
            if (validateAllFields()) {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()
                val firstName = binding.etFirstName.text.toString().trim()
                val lastName = binding.etLastName.text.toString().trim()

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firestore = FirebaseFirestore.getInstance()
                            val user = task.result?.user
                            val uid = user?.uid

                            // Estos datos son persistidos en Firestore Database
                            val userData = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "email" to email,
                                "role" to UserRole.ASSISTANT.value
                            )

                            if (uid != null) {
                                firestore.collection("users").document(uid).set(userData)
                                    .addOnSuccessListener {
                                        user.sendEmailVerification()
                                            .addOnCompleteListener { emailTask ->
                                                if (emailTask.isSuccessful) {
                                                    Snackbar.make(
                                                        binding.root,
                                                        "Registro exitoso. Revise su correo para verificar su cuenta.",
                                                        Snackbar.LENGTH_LONG
                                                    ).show()

                                                    // Redirige al usuario hacia el login
                                                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                                                } else {
                                                    Log.e(
                                                        "SignupFragment",
                                                        "Error al enviar correo de verificación",
                                                        emailTask.exception
                                                    )
                                                    Snackbar.make(
                                                        binding.root,
                                                        "Error al enviar correo de verificación.",
                                                        Snackbar.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                    }.addOnFailureListener { e ->
                                        Log.e(
                                            "Signup",
                                            "Error al guardar datos en Firestore Database",
                                            e
                                        )
                                        Snackbar.make(
                                            binding.root,
                                            "Error al registrar usuario. Por favor, intenta nuevamente.",
                                            Snackbar.LENGTH_LONG
                                        ).show()
                                    }
                            }

                        } else {
                            val exception = task.exception
                            if (exception is FirebaseAuthUserCollisionException) {
                                binding.tilEmail.error = "Correo electrónico ya registrado"
                            } else {
                                Log.e("Signup", "Error al registrar usuario", exception)
                                Snackbar.make(
                                    binding.root,
                                    "Error al registrar usuario. Por favor, intenta nuevamente.",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
            }
        }

        binding.tvAlreadyHaveAccount.setOnClickListener {
            val firstName = binding.etFirstName.text.toString().trim()
            val lastName = binding.etLastName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            val anyFieldNotEmpty =
                firstName.isNotEmpty() || lastName.isNotEmpty() || email.isNotEmpty() || password.isNotEmpty() || confirmPassword.isNotEmpty()

            if (anyFieldNotEmpty) {
                AlertDialog.Builder(requireContext()).setTitle("¿Desea salir del registro?")
                    .setMessage("Se perderán los datos ingresados. ¿Está seguro de que desea salir?")
                    .setPositiveButton("Sí") { _, _ ->
                        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                    }.setNegativeButton("No", null).show()
            } else {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Valida todos los campos del formulario de registro.
     *
     * Esta funcion verifica que cada campo cumpla con sus criterios de validacion específicos,
     * mostrando mensajes de error correspondientes en los TextInputLayout asociados.
     *
     * @return `true` si todos los campos son validos, `false` si alguno no cumple la validacion.
     */
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

    /**
     * Configura listeners para que, cuando el usuario modifique el texto en cualquiera
     * de los campos de entrada, se borre el mensaje de error correspondiente en el
     * `TextInputLayout` asociado.
     *
     * Esto mejora la experiencia de usuario al eliminar los errores visibles en cuanto
     * el usuario comienza a corregir el campo.
     */
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
                binding.tilConfirmPassword.error = null // Si esta vacio, no muestra el error
            }
        }
    }

}