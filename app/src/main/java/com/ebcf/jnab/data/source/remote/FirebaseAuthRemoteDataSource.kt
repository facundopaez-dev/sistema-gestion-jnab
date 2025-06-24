package com.ebcf.jnab.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class FirebaseAuthRemoteDataSource private constructor() {

    private val auth = FirebaseAuth.getInstance()

    fun logout() {
        auth.signOut()
    }

    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val exception = task.exception ?: Exception("Error desconocido")
                    Log.e(
                        "AuthRemoteDataSource",
                        "Error al enviar correo de recuperación",
                        exception
                    )
                    onError(exception)
                }
            }
    }

    // Objeto companion que implementa el patron singleton manualmente.
    // Se asegura que solo haya una unica instancia de esta clase.
    // Si se usa Hilt o Koin, esto debe ser eliminado.
    companion object {
        @Volatile
        private var instance: FirebaseAuthRemoteDataSource? = null

        fun getInstance(): FirebaseAuthRemoteDataSource {
            return instance ?: synchronized(this) {
                instance ?: FirebaseAuthRemoteDataSource().also { instance = it }
            }
        }
    }
}