package com.ebcf.jnab.domain.repository

interface AuthRepository {
    fun logout()
    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )
}