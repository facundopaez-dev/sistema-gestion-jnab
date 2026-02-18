package com.ebcf.jnab.data.repository

import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource
import com.ebcf.jnab.data.source.remote.FirebaseFirestoreProvider
import com.ebcf.jnab.domain.model.AuthError
import com.ebcf.jnab.domain.repository.AuthRepository
import com.ebcf.jnab.data.common.USERS
import com.ebcf.jnab.data.common.EMAIL
import com.ebcf.jnab.data.common.FIRST_NAME
import com.ebcf.jnab.data.common.LAST_NAME
import com.ebcf.jnab.data.common.ROLE
import com.ebcf.jnab.domain.model.LoginResult
import com.ebcf.jnab.domain.model.User

class AuthRepositoryImpl(
    private val remoteDataSource: FirebaseAuthRemoteDataSource
) : AuthRepository {

    override fun login(
        email: String,
        password: String,
        onResult: (LoginResult) -> Unit
    ) {
        remoteDataSource.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onResult(LoginResult.Error(AuthError.InvalidCredentials))
                    return@addOnCompleteListener
                }

                val firebaseUser = remoteDataSource.auth.currentUser

                if (firebaseUser == null) {
                    onResult(LoginResult.Error(AuthError.InvalidCredentials))
                    return@addOnCompleteListener
                }

                // Eliminamos temporalmente la verificación obligatoria de email para permitir el login
                // if (!firebaseUser.isEmailVerified) { ... }

                FirebaseFirestoreProvider.provide()
                    .collection(USERS)
                    .document(firebaseUser.uid)
                    .get()
                    .addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            onResult(LoginResult.Error(AuthError.GenericError))
                            return@addOnSuccessListener
                        }

                        val email = doc.getString(EMAIL)
                        val firstName = doc.getString(FIRST_NAME)
                        val lastName = doc.getString(LAST_NAME)
                        val role = doc.getString(ROLE)

                        if (role == null || email == null || firstName == null || lastName == null) {
                            onResult(LoginResult.Error(AuthError.GenericError))
                            return@addOnSuccessListener
                        }

                        val user = User(
                            uid = firebaseUser.uid,
                            email = email,
                            firstName = firstName,
                            lastName = lastName,
                            role = role
                        )

                        onResult(LoginResult.Success(user))
                    }
                    .addOnFailureListener {
                        onResult(LoginResult.Error(AuthError.GenericError))
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