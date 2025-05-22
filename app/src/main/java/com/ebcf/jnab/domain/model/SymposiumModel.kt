package com.ebcf.jnab.domain.model

import java.time.LocalDateTime

data class SymposiumModel(
    val id : Int,
    val title: String,
    val description: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val talks: List<TalkModel>

)