package com.ebcf.jnab.domain.model

data class InscripcionItem(
    val userId: String,
    val nombreCompleto: String,
    val estado: String,
    val fechaRegistro: String?,
    val comprobanteBase64: String,
    var expanded: Boolean = false
)
