package com.ebcf.jnab.domain.model

enum class RejectionReason(val label: String) {
    OUT_OF_SCOPE("Fuera del alcance del simposio"),
    INSUFFICIENT_QUALITY("Calidad insuficiente"),
    INCOMPLETE_SUBMISSION("Trabajo incompleto"),
    LACKS_ORIGINALITY("Falta de originalidad"),
    OTHER("Otro motivo");

    override fun toString(): String = label
}