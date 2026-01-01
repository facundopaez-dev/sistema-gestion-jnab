package com.ebcf.jnab.data.repository

import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource
import com.ebcf.jnab.data.source.remote.FirebaseFirestoreProvider
import com.ebcf.jnab.domain.model.AuthError
import com.ebcf.jnab.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: FirebaseAuthRemoteDataSource
) : AuthRepository {

    override fun login(
        email: String,
        password: String,
        onSuccess: (role: String) -> Unit,
        onError: (error: AuthError) -> Unit
    ) {
        remoteDataSource.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onError(AuthError.InvalidCredentials)
                    return@addOnCompleteListener
                }

                val user = remoteDataSource.auth.currentUser
                if (user == null || !user.isEmailVerified) {
                    onError(AuthError.InvalidCredentials)
                    return@addOnCompleteListener
                }

                FirebaseFirestoreProvider.provide().collection("users")
                    .document(user.uid)
                    .get()
                    .addOnSuccessListener { doc ->
                        if (doc.getString("role") != null) {
                            onSuccess(doc.getString("role")!!)
                        } else {
                            onError(AuthError.GenericError)
                        }
                    }
                    .addOnFailureListener {
                        onError(AuthError.GenericError)
                    }
            }
    }

    override fun logout() {
        remoteDataSource.logout()
    }

    override fun sendPasswordResetEmail(
        email: String, onSuccess: () -> Unit, onError: (Exception) -> Unit
    ) {
        remoteDataSource.sendPasswordResetEmail(email, onSuccess, onError)
    }
}