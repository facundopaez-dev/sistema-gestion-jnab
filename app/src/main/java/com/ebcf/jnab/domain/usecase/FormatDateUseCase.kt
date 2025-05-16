package com.ebcf.jnab.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class FormatDateUseCase {

    @RequiresApi(Build.VERSION_CODES.O)
    fun execute(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern(
            "d 'de' MMMM 'de' yyyy, HH:mm 'hs'",
            Locale("es", "ES")
        )
        return dateTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatRange(start: LocalDateTime, end: LocalDateTime): String {
        val dateFormatter = DateTimeFormatter.ofPattern(
            "d 'de' MMMM 'de' yyyy",
            Locale("es", "ES")
        )
        val timeFormatter = DateTimeFormatter.ofPattern(
            "HH:mm 'hs'",
            Locale("es", "ES")
        )

        return if (start.toLocalDate() == end.toLocalDate()) {
            // Misma fecha
            "${start.format(dateFormatter)}, ${start.format(timeFormatter)} a ${end.format(timeFormatter)}"
        } else {
            // Fechas diferentes
            "${start.format(dateFormatter)}, ${start.format(timeFormatter)} a ${end.format(dateFormatter)}, ${end.format(timeFormatter)}"
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateTime(date: java.time.LocalDate, time: java.time.LocalTime): String {
        val dateFormatter = DateTimeFormatter.ofPattern(
            "d 'de' MMMM 'de' yyyy",
            Locale("es", "ES")
        )
        val timeFormatter = DateTimeFormatter.ofPattern(
            "HH:mm 'hs'",
            Locale("es", "ES")
        )

        val formattedDate = date.format(dateFormatter)
        val formattedTime = time.format(timeFormatter)

        return "$formattedDate, $formattedTime"
    }
}