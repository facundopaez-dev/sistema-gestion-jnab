package com.ebcf.jnab.domain.repository

import com.ebcf.jnab.domain.model.AuthError

interface AuthRepository {

    fun login(
        email: String,
        password: String,
        onSuccess: (role: String) -> Unit,
        onError: (error: AuthError) -> Unit
    )

    fun logout()
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )
}