package com.ebcf.jnab.ui.signup

import android.util.Patterns
import com.ebcf.jnab.util.ERROR_INVALID_EMAIL
import com.ebcf.jnab.util.ERROR_INVALID_PASSWORD
import com.ebcf.jnab.util.MIN_PASSWORD_LENGTH

private const val ERROR_INVALID_FIRST_NAME = "Nombre inválido"
private const val ERROR_INVALID_LAST_NAME = "Apellido inválido"
private const val ERROR_INVALID_CONFIRM_PASSWORD = "Las contraseñas no coinciden"

private val NAME_PATTERN =
    "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(?:[- ][A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$".toRegex()

data class SignupUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
) {

    fun validateFirstName() =
        copy(firstNameError =
            if (NAME_PATTERN.matches(firstName)) null else ERROR_INVALID_FIRST_NAME)

    fun validateLastName() =
        copy(lastNameError =
            if (NAME_PATTERN.matches(lastName)) null else ERROR_INVALID_LAST_NAME)

    fun validateEmail() =
        copy(emailError =
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else ERROR_INVALID_EMAIL)

    fun validatePassword() =
        copy(passwordError =
            if (password.length >= MIN_PASSWORD_LENGTH) null else ERROR_INVALID_PASSWORD)

    fun validateConfirmPassword() =
        copy(confirmPasswordError =
            if (password == confirmPassword) null else ERROR_INVALID_CONFIRM_PASSWORD)

    fun isFormValid(): Boolean =
        listOf(
            firstNameError,
            lastNameError,
            emailError,
            passwordError,
            confirmPasswordError
        ).all { it == null } &&
                firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                email.isNotBlank()
}