package com.ebcf.jnab.ui.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.snackbar.Snackbar

/* -------------------- */
/* SCREEN (con ViewModel) */
/* -------------------- */

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
                        (context as android.app.Activity)
                            .findViewById(android.R.id.content),
                        "Registro exitoso. Revise su correo para verificar su cuenta.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    onNavigateToLogin()
                }

                SignupMessage.SignupError -> {
                    Snackbar.make(
                        (context as android.app.Activity)
                            .findViewById(android.R.id.content),
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

    SignupContent(
        uiState = uiState,
        signupState = signupState,
        onFirstNameChange = {
            uiState = uiState.copy(firstName = it, firstNameError = null)
        },
        onLastNameChange = {
            uiState = uiState.copy(lastName = it, lastNameError = null)
        },
        onEmailChange = {
            uiState = uiState.copy(email = it, emailError = null)
        },
        onPasswordChange = {
            uiState = uiState.copy(password = it, passwordError = null)
        },
        onConfirmPasswordChange = {
            uiState = uiState.copy(confirmPassword = it)
            uiState = uiState.validateConfirmPassword()
        },
        onFirstNameFocusLost = { uiState = uiState.validateFirstName() },
        onLastNameFocusLost = { uiState = uiState.validateLastName() },
        onEmailFocusLost = { uiState = uiState.validateEmail() },
        onPasswordFocusLost = { uiState = uiState.validatePassword() },
        onConfirmPasswordFocusLost = { uiState = uiState.validateConfirmPassword() },
        onSignupClick = {
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
        },
        onNavigateToLogin = onNavigateToLogin
    )
}

/* -------------------- */
/* CONTENT (UI pura)    */
/* -------------------- */

@Composable
fun SignupContent(
    uiState: SignupUiState,
    signupState: SignupState?,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onFirstNameFocusLost: () -> Unit,
    onLastNameFocusLost: () -> Unit,
    onEmailFocusLost: () -> Unit,
    onPasswordFocusLost: () -> Unit,
    onConfirmPasswordFocusLost: () -> Unit,
    onSignupClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
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
            onValueChange = onFirstNameChange,
            onFocusLost = onFirstNameFocusLost
        )

        SignupTextField(
            value = uiState.lastName,
            label = "Apellido",
            error = uiState.lastNameError,
            onValueChange = onLastNameChange,
            onFocusLost = onLastNameFocusLost
        )

        SignupTextField(
            value = uiState.email,
            label = "Email",
            error = uiState.emailError,
            onValueChange = onEmailChange,
            onFocusLost = onEmailFocusLost
        )

        SignupTextField(
            value = uiState.password,
            label = "Contraseña",
            error = uiState.passwordError,
            isPassword = true,
            onValueChange = onPasswordChange,
            onFocusLost = onPasswordFocusLost
        )

        SignupTextField(
            value = uiState.confirmPassword,
            label = "Confirmar contraseña",
            error = uiState.confirmPasswordError,
            isPassword = true,
            onValueChange = onConfirmPasswordChange,
            onFocusLost = onConfirmPasswordFocusLost
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = signupState !is SignupState.Loading,
            onClick = onSignupClick
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Tiene una cuenta? Inicie sesión")
        }
    }
}

/* -------------------- */
/* PREVIEW              */
/* -------------------- */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupScreenPreview() {
    MaterialTheme {
        SignupContent(
            uiState = SignupUiState(
                firstName = "John",
                lastName = "Doe",
                email = "john@email.com",
                password = "123456",
                confirmPassword = "123456"
            ),
            signupState = null,
            onFirstNameChange = {},
            onLastNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onFirstNameFocusLost = {},
            onLastNameFocusLost = {},
            onEmailFocusLost = {},
            onPasswordFocusLost = {},
            onConfirmPasswordFocusLost = {},
            onSignupClick = {},
            onNavigateToLogin = {}
        )
    }
}