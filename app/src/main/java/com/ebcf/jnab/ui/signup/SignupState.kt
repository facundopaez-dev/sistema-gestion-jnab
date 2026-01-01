package com.ebcf.jnab.ui.signup

sealed class SignupState {
    object Loading : SignupState()
    data class Result(val message: SignupMessage) : SignupState()
}