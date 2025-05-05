package com.ebcf.jnab.data.model

import java.time.LocalDateTime

data class SymposiumModel(
    val id : Int,
    val title: String,
    val description: String,
    val dateTime: LocalDateTime
)