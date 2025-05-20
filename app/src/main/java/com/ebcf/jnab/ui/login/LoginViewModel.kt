package com.ebcf.jnab.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.utils.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val ERROR_LOGIN = "Correo electrónico o contraseña incorrectos"
private const val ERROR_GENERIC = "Error al iniciar sesión. Por favor, inténtelo más tarde."

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
                        loginError.postValue(ERROR_LOGIN)
                    }
                } else {
                    Log.e("LoginViewModel", "Usuario null tras login exitoso para email: $email")
                    loginError.postValue(ERROR_GENERIC)
                }
            } else {
                loginError.postValue(ERROR_LOGIN)
            }
        }
    }

    private fun getUserRoleFromFirestore(uid: String) {
        firestore.collection("users").document(uid).get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {

                if (document.getString("role") != null) {
                    loginSuccess.postValue(document.getString("role"))
                } else {
                    Log.e("LoginViewModel", "El documento del usuario con UID $uid no contiene campo 'role'")
                    loginError.postValue(ERROR_GENERIC)
                }

            } else {
                Log.e("LoginViewModel", "No se encontró el documento del usuario con UID: $uid")
                loginError.postValue(ERROR_GENERIC)
            }
        }.addOnFailureListener { e ->
            Log.e("LoginViewModel", "Error al obtener el documento del usuario: ${e.message}", e)
            loginError.postValue(ERROR_GENERIC)
        }
    }
}