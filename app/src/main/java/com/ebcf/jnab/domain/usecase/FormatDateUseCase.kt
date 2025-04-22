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
}