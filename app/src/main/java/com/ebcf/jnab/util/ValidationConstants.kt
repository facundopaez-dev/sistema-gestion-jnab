package com.ebcf.jnab.util

const val MIN_PASSWORD_LENGTH = 6
const val ERROR_INVALID_EMAIL = "Correo electrónico no válido"

// Propiedad calculada que usa MIN_PASSWORD_LENGTH dinamicamente
val ERROR_INVALID_PASSWORD: String
    get() = "La contraseña debe tener al menos $MIN_PASSWORD_LENGTH caracteres"