package com.ebcf.jnab.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class TalkModel (
    val id: Int,
    val symposiumId: Int,
    val title: String,
    val description: String,
    val speakerId: Int, //TODO: luego lo cambiamos por un objeto Speaker.
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)