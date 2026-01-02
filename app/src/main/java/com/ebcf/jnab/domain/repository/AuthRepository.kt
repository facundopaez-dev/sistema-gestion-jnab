package com.ebcf.jnab.domain.repository

import com.ebcf.jnab.domain.model.LoginResult

interface AuthRepository {

    fun login(
        email: String,
        password: String,
        onResult: (LoginResult) -> Unit
    )

    fun logout()
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )
}