package com.ebcf.jnab.data.model

import java.time.LocalDateTime

data class TalkModel (
    val id: Number,
    val title: String,
    val description: String,
    val author: String, //TODO: luego lo cambiamos por un objeto Speaker.
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)