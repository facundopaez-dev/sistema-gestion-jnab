package com.ebcf.jnab.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.auth0.jwt.JWT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    getUserRole(user)
                } else {
                    _loginResult.value = LoginResult(false, task.exception?.message)
                }
            }
    }

    private fun getUserRole(user: FirebaseUser?) {
        user?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result?.token
                // Decodifica el token JWT para extraer los claims
                val role = extractRoleFromToken(token)
                _loginResult.value = LoginResult(true, role = role)
            } else {
                _loginResult.value = LoginResult(false, "Error al obtener el rol")
            }
        }
    }

    private fun extractRoleFromToken(token: String?): String {
        if (token != null) {
            // Decodifica el JWT usando una librería externa para obtener los claims
            val decodedToken = decodeJwt(token)

            // Extrae el rol del token decodificado
            return decodedToken["role"] ?: "user"  // Default a "user" si no se encuentra el claim
        }

        return "user"  // Default a "user" si no hay token
    }

    private fun decodeJwt(token: String?): Map<String, String> {
        // Utiliza una librería para decodificar el JWT y extraer los claims
        val jwtDecoder = JWT.decode(token)
        val claims = jwtDecoder.claims
        val roleClaim = claims["role"]?.asString() ?: "user"
        return mapOf("role" to roleClaim)
    }
}

data class LoginResult(val success: Boolean, val message: String? = null, val role: String = "user")
