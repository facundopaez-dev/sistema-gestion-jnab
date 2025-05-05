package com.ebcf.jnab.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ebcf.jnab.data.model.SymposiumModel
import java.time.LocalDateTime

class SymposiumRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    private val hardcodedSymposia = listOf(
        SymposiumModel(
            id = 1,
            title   = "Antropología Biológica 2025",
            description = "Un simposio sobre avances en la investigación de la antropología biológica.",
            dateTime = LocalDateTime.of(2025, 4, 22, 9, 0)
        ),
        SymposiumModel(
            id = 1,
            title   = "Evolución Humana",
            description = "Simposio que cubre temas relacionados con la evolución humana.",
            dateTime = LocalDateTime.of(2025, 4, 23, 10, 0)
        ),
        SymposiumModel(
            id = 1,
            title   = "Biodiversidad Humana",
            description = "Un análisis sobre la biodiversidad en los seres humanos.",
            dateTime = LocalDateTime.of(2025, 4, 23, 14, 0)
        ),
        SymposiumModel(
            id = 1,
            title  = "Genómica y Adaptación Humana",
            description = "Exploración de cómo la genómica ha influido en la adaptación humana.",
            dateTime = LocalDateTime.of(2025, 4, 24, 11, 30)
        ),
        SymposiumModel(
            id = 1,
            title  = "Simposio de Paleontología y Comportamiento",
            description = "Discusión sobre el comportamiento humano a través de hallazgos paleontológicos.",
            dateTime = LocalDateTime.of(2025, 4, 25, 13, 0)
        ),
        SymposiumModel(
            id = 1,
            title  = "Migraciones Humanas Antiguas",
            description = "Estudio de las migraciones humanas prehistóricas y su impacto genético.",
            dateTime = LocalDateTime.of(2025, 4, 25, 18, 30)
        ),
        SymposiumModel(
            id = 1,
            title  = "Simposio de Antropología Forense",
            description = "Avances en técnicas forenses para el estudio de restos humanos.",
            dateTime = LocalDateTime.of(2025, 4, 26, 9, 0)
        ),
        SymposiumModel(
            id = 1,
            title  = "Evolución de la Dieta Humana",
            description = "Cómo la dieta ha moldeado la evolución humana a lo largo del tiempo.",
            dateTime = LocalDateTime.of(2025, 4, 26, 13, 0)
        ),
        SymposiumModel(
            id = 1,
            title  = "Simposio de Epigenética Humana",
            description = "Exploración de la epigenética y su rol en la variación humana.",
            dateTime = LocalDateTime.of(2025, 4, 26, 16, 30)
        ),
        SymposiumModel(
            id = 1,
            title  = "Diversidad Cultural y Biológica",
            description = "Relación entre la diversidad cultural y la biología humana.",
            dateTime = LocalDateTime.of(2025, 4, 26, 18, 0)
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(): List<SymposiumModel> {
        return hardcodedSymposia.sortedBy { it.dateTime }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNextSymposiums(): List<SymposiumModel> {
        val currentDateTime = LocalDateTime.now() // Obtiene la fecha y hora actuales
        return hardcodedSymposia
            // Compara la fecha y hora de cada simposio con la fecha y hora actuales
            .filter { symposium -> symposium.dateTime.isAfter(currentDateTime) }
            .sortedBy { it.dateTime }
    }

}