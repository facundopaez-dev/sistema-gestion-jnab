package com.ebcf.jnab.domain.model

enum class SubmissionStatus(val displayName: String) {
    PENDING("Pendiente"),
    APPROVED("Aprobado"),
    REJECTED("Rechazado")
}