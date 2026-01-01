package com.ebcf.jnab.ui.signup

sealed class SignupMessage {
    object SignupSuccess : SignupMessage()
    object SignupError : SignupMessage()
    object EmailAlreadyRegistered : SignupMessage()
}