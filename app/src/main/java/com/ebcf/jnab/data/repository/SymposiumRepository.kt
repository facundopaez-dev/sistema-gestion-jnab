package com.ebcf.jnab.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ebcf.jnab.data.model.Symposium
import java.time.LocalDateTime

class SymposiumRepository {

    // Lista de simposios hardcodeados
    @RequiresApi(Build.VERSION_CODES.O)
    private val hardcodedSymposia = listOf(
        Symposium(
            title = "Simposio de Antropología Biológica 2025",
            description = "Un simposio sobre avances en la investigación de la antropología biológica.",
            dateTime = LocalDateTime.of(2025, 5, 1, 9, 0)
        ),
        Symposium(
            title = "Simposio sobre Evolución Humana",
            description = "Simposio que cubre temas relacionados con la evolución humana.",
            dateTime = LocalDateTime.of(2024, 11, 15, 10, 0)
        ),
        Symposium(
            title = "Simposio de Biodiversidad Humana",
            description = "Un análisis sobre la biodiversidad en los seres humanos.",
            dateTime = LocalDateTime.of(2025, 7, 20, 14, 0)
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(): List<Symposium> {
        return hardcodedSymposia
    }

}