package com.ebcf.jnab.domain.model

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data class Error(val error: AuthError) : LoginResult()
}