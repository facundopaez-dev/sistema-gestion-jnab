package com.ebcf.jnab.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    // Definir el resultado de la autenticación
    private val _loginResult = MutableLiveData<Result>()
    val loginResult: LiveData<Result> get() = _loginResult

    // FirebaseAuth instance
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Función para hacer login
    fun loginUser(email: String, password: String) {
        // Verificar que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            _loginResult.value = Result.Error("El correo o la contraseña no pueden estar vacíos")
            return
        }

        // Intentar iniciar sesión con Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login exitoso
                    _loginResult.value = Result.Success
                } else {
                    // Si ocurre un error en el login
                    _loginResult.value = Result.Error("Error de autenticación: ${task.exception?.message}")
                }
            }
            .addOnFailureListener { exception ->
                // Si ocurre una falla general
                _loginResult.value = Result.Error("Error de conexión: ${exception.message}")
            }
    }

    // Sellar el resultado como una clase sellada para manejo de éxito y error
    sealed class Result {
        data object Success : Result()
        data class Error(val message: String) : Result()
    }
}
