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
            title = "Antropología Biológica 2025",
            description = "Un simposio sobre avances en la investigación de la antropología biológica.",
            startDateTime = LocalDateTime.of(2025, 4, 22, 9, 0),
            endDateTime = LocalDateTime.of(2025, 4, 22, 11, 0),
            talks = emptyList()
        ),
        SymposiumModel(
            id = 2,
            title = "Evolución Humana",
            description = "Simposio que cubre temas relacionados con la evolución humana.",
            startDateTime = LocalDateTime.of(2025, 4, 23, 10, 0),
            endDateTime = LocalDateTime.of(2025, 4, 23, 12, 0),
            talks = emptyList()
        ),
        SymposiumModel(
            id = 3,
            title = "Biodiversidad Humana",
            description = "Un análisis sobre la biodiversidad en los seres humanos.",
            startDateTime = LocalDateTime.of(2025, 4, 23, 14, 0),
            endDateTime = LocalDateTime.of(2025, 4, 23, 16, 0),
            talks = emptyList()
        ),
        SymposiumModel(
            id = 4,
            title = "Genómica y Adaptación Humana",
            description = "Exploración de cómo la genómica ha influido en la adaptación humana.",
            startDateTime = LocalDateTime.of(2025, 4, 24, 11, 30),
            endDateTime = LocalDateTime.of(2025, 4, 24, 13, 30),
            talks = emptyList()
        ),
        SymposiumModel(
            id = 5,
            title = "Simposio de Paleontología y Comportamiento",
            description = "Discusión sobre el comportamiento humano a través de hallazgos paleontológicos.",
            startDateTime = LocalDateTime.of(2025, 4, 25, 13, 0),
            endDateTime = LocalDateTime.of(2025, 4, 25, 15, 0),
            talks = emptyList()
        ),
        SymposiumModel(
            id = 6,
            title = "Migraciones Humanas Antiguas",
            description = "Estudio de las migraciones humanas prehistóricas y su impacto genético.",
            startDateTime = LocalDateTime.of(2025, 4, 25, 18, 30),
            endDateTime = LocalDateTime.of(2025, 4, 25, 20, 30),
            talks = emptyList()
        ),
        SymposiumModel(
            id = 7,
            title = "Simposio de Antropología Forense",
            description = "Avances en técnicas forenses para el estudio de restos humanos.",
            startDateTime = LocalDateTime.of(2025, 4, 26, 9, 0),
            endDateTime = LocalDateTime.of(2025, 4, 26, 11, 0),
            talks = emptyList()
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(): List<SymposiumModel> {
        return hardcodedSymposia.sortedBy { it.startDateTime }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNextSymposiums(): List<SymposiumModel> {
        val currentDateTime = LocalDateTime.now() // Obtiene la fecha y hora actuales
        return hardcodedSymposia
            // Compara la fecha y hora de cada simposio con la fecha y hora actuales
            .filter { symposium -> symposium.startDateTime.isAfter(currentDateTime) }
            .sortedBy { it.startDateTime }
    }

}