package com.ebcf.jnab.ui.signup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun SignupTextField(
    value: String,
    label: String,
    error: String?,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit,
    onFocusLost: () -> Unit
) {
    var hadFocus by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        visualTransformation =
            if (isPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (hadFocus && !it.isFocused) {
                    onFocusLost()
                }
                hadFocus = it.isFocused
            }
    )

    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}