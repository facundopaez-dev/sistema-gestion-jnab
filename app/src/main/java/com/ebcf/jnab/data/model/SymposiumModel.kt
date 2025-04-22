package com.ebcf.jnab.data.model

import java.time.LocalDateTime

data class SymposiumModel(
    val title: String,
    val description: String,
    val dateTime: LocalDateTime
)