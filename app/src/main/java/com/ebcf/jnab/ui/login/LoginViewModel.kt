package com.ebcf.jnab.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ebcf.jnab.utils.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val loginSuccess = SingleLiveEvent<String>() // Para emitir el rol
    val loginError = SingleLiveEvent<String>()

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    if (user.isEmailVerified) {
                        getUserRoleFromFirestore(user.uid)
                    } else {
                        val msg = "Correo electrónico o contraseña incorrectos."
                        loginError.postValue(msg)
                    }
                } else {
                    val msg = "Error al iniciar sesión. Por favor, inténtelo más tarde."
                    Log.e("LoginViewModel", "Usuario null tras login exitoso para email: $email")
                    loginError.postValue(msg)
                }
            } else {
                val msg = "Correo electrónico o contraseña incorrectos."
                loginError.postValue(msg)
            }
        }
    }

    private fun getUserRoleFromFirestore(uid: String) {
        firestore.collection("users").document(uid).get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val role = document.getString("role") ?: "user"
                _loginResult.value = LoginResult(true, role = role)
                loginSuccess.postValue(role)
            } else {
                val msg = "Error al iniciar sesión. Por favor, inténtelo más tarde."
                Log.e("LoginViewModel", "No se encontró el documento del usuario con UID: $uid")
                loginError.postValue(msg)
            }
        }.addOnFailureListener { e ->
            val msg = "Error al iniciar sesión. Por favor, inténtelo más tarde."
            Log.e("LoginViewModel", "Error al obtener el documento del usuario: ${e.message}", e)
            loginError.postValue(msg)
        }
    }
}

data class LoginResult(val success: Boolean, val role: String = "user")