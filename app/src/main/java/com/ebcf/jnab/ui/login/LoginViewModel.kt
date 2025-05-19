package com.ebcf.jnab.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.utils.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {

    val loginSuccess = SingleLiveEvent<String>() // Emite el rol en inicio de sesion exitoso
    val loginError = SingleLiveEvent<String>() // Emite mensaje de error

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