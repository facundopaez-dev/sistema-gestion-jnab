package com.ebcf.jnab.ui.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.material.snackbar.Snackbar

@Composable
fun SignupScreen(
    viewModel: SignupViewModel,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val signupState by viewModel.signupState.observeAsState()

    var uiState by remember { mutableStateOf(SignupUiState()) }

    LaunchedEffect(signupState) {
        if (signupState is SignupState.Result) {
            when ((signupState as SignupState.Result).message) {
                SignupMessage.SignupSuccess -> {
                    Snackbar.make(
                        (context as android.app.Activity).findViewById(android.R.id.content),
                        "Registro exitoso. Revise su correo para verificar su cuenta.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    onNavigateToLogin()
                }

                SignupMessage.SignupError -> {
                    Snackbar.make(
                        (context as android.app.Activity).findViewById(android.R.id.content),
                        "Error al registrar usuario.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                SignupMessage.EmailAlreadyRegistered -> {
                    uiState = uiState.copy(
                        emailError = "Correo electrónico ya registrado"
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        SignupTextField(
            value = uiState.firstName,
            label = "Nombre",
            error = uiState.firstNameError,
            onValueChange = {
                uiState = uiState.copy(firstName = it, firstNameError = null)
            },
            onFocusLost = { uiState = uiState.validateFirstName() }
        )

        SignupTextField(
            value = uiState.lastName,
            label = "Apellido",
            error = uiState.lastNameError,
            onValueChange = {
                uiState = uiState.copy(lastName = it, lastNameError = null)
            },
            onFocusLost = { uiState = uiState.validateLastName() }
        )

        SignupTextField(
            value = uiState.email,
            label = "Email",
            error = uiState.emailError,
            onValueChange = {
                uiState = uiState.copy(email = it, emailError = null)
            },
            onFocusLost = { uiState = uiState.validateEmail() }
        )

        SignupTextField(
            value = uiState.password,
            label = "Contraseña",
            error = uiState.passwordError,
            isPassword = true,
            onValueChange = {
                uiState = uiState.copy(password = it, passwordError = null)
            },
            onFocusLost = { uiState = uiState.validatePassword() }
        )

        SignupTextField(
            value = uiState.confirmPassword,
            label = "Confirmar contraseña",
            error = uiState.confirmPasswordError,
            isPassword = true,
            onValueChange = {
                uiState = uiState.copy(confirmPassword = it)
                uiState = uiState.validateConfirmPassword()
            },
            onFocusLost = { uiState = uiState.validateConfirmPassword() }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = signupState !is SignupState.Loading,
            onClick = {
                uiState = uiState
                    .validateFirstName()
                    .validateLastName()
                    .validateEmail()
                    .validatePassword()
                    .validateConfirmPassword()

                if (uiState.isFormValid()) {
                    viewModel.signup(
                        uiState.email.trim(),
                        uiState.password,
                        uiState.firstName.trim(),
                        uiState.lastName.trim()
                    )
                }
            }
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tenés una cuenta?")
        }
    }
}