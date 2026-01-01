package com.ebcf.jnab.domain.model

sealed class AuthError {
    object InvalidCredentials : AuthError()
    object GenericError : AuthError()
}