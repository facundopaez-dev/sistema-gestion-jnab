package com.ebcf.jnab.data.repository

import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource
import com.ebcf.jnab.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: FirebaseAuthRemoteDataSource
) : AuthRepository {

    override fun sendPasswordResetEmail(
        email: String, onSuccess: () -> Unit, onError: (Exception) -> Unit
    ) {
        remoteDataSource.sendPasswordResetEmail(email, onSuccess, onError)
    }
}