package com.ebcf.jnab.ui.login

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ebcf.jnab.util.ERROR_INVALID_EMAIL
import com.ebcf.jnab.util.ERROR_INVALID_PASSWORD
import com.ebcf.jnab.util.MIN_PASSWORD_LENGTH

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onSignupClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null   // equivalente a clearErrorOnTextChange
            },
            label = { Text("Correo electrónico") },
            isError = emailError != null,
            modifier = Modifier.fillMaxWidth()
        )

        emailError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            modifier = Modifier.fillMaxWidth()
        )

        passwordError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val isEmailValid =
                    Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
                val isPasswordValid =
                    password.length >= MIN_PASSWORD_LENGTH

                emailError =
                    if (!isEmailValid) ERROR_INVALID_EMAIL else null
                passwordError =
                    if (!isPasswordValid) ERROR_INVALID_PASSWORD else null

                if (isEmailValid && isPasswordValid) {
                    viewModel.login(email.trim(), password.trim())
                }
            }
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onForgotPasswordClick) {
            Text("¿Olvido su contraseña?")
        }

        TextButton(onClick = onSignupClick) {
            Text("Registrarse")
        }
    }
}