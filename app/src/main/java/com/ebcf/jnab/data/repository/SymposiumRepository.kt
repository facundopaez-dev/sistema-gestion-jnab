package com.ebcf.jnab.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ebcf.jnab.data.model.SymposiumModel
import java.time.LocalDateTime

class SymposiumRepository {

    // Lista de simposios hardcodeados
    @RequiresApi(Build.VERSION_CODES.O)
    private val hardcodedSymposia = listOf(
        SymposiumModel(
            title = "Simposio de Antropología Biológica 2025",
            description = "Un simposio sobre avances en la investigación de la antropología biológica.",
            dateTime = LocalDateTime.of(2025, 5, 1, 9, 0)
        ),
        SymposiumModel(
            title = "Simposio sobre Evolución Humana",
            description = "Simposio que cubre temas relacionados con la evolución humana.",
            dateTime = LocalDateTime.of(2024, 11, 15, 10, 0)
        ),
        SymposiumModel(
            title = "Simposio de Biodiversidad Humana",
            description = "Un análisis sobre la biodiversidad en los seres humanos.",
            dateTime = LocalDateTime.of(2025, 7, 20, 14, 0)
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private val simposiosProximos = listOf(
        SymposiumModel(
            title = "Simposio de Antropología Biológica 2025",
            description = "Un simposio sobre avances en la investigación de la antropología biológica.",
            dateTime = LocalDateTime.of(2025, 5, 1, 9, 0)
        ),
        SymposiumModel(
            title = "Simposio sobre Evolución Humana",
            description = "Simposio que cubre temas relacionados con la evolución humana.",
            dateTime = LocalDateTime.of(2024, 11, 15, 10, 0)
        ),
        SymposiumModel(
            title = "Simposio de Biodiversidad Humana",
            description = "Un análisis sobre la biodiversidad en los seres humanos.",
            dateTime = LocalDateTime.of(2025, 7, 20, 14, 0)
        ),
        SymposiumModel(
            title = "Genómica y Adaptación Humana",
            description = "Exploración de cómo la genómica ha influido en la adaptación humana.",
            dateTime = LocalDateTime.of(2025, 3, 10, 11, 30)
        ),
        SymposiumModel(
            title = "Simposio de Paleontología y Comportamiento",
            description = "Discusión sobre el comportamiento humano a través de hallazgos paleontológicos.",
            dateTime = LocalDateTime.of(2025, 6, 5, 13, 0)
        ),
        SymposiumModel(
            title = "Migraciones Humanas Antiguas",
            description = "Estudio de las migraciones humanas prehistóricas y su impacto genético.",
            dateTime = LocalDateTime.of(2025, 9, 12, 9, 30)
        ),
        SymposiumModel(
            title = "Simposio de Antropología Forense",
            description = "Avances en técnicas forenses para el estudio de restos humanos.",
            dateTime = LocalDateTime.of(2025, 4, 18, 15, 0)
        ),
        SymposiumModel(
            title = "Evolución de la Dieta Humana",
            description = "Cómo la dieta ha moldeado la evolución humana a lo largo del tiempo.",
            dateTime = LocalDateTime.of(2025, 8, 25, 10, 0)
        ),
        SymposiumModel(
            title = "Simposio de Epigenética Humana",
            description = "Exploración de la epigenética y su rol en la variación humana.",
            dateTime = LocalDateTime.of(2025, 10, 3, 14, 30)
        ),
        SymposiumModel(
            title = "Diversidad Cultural y Biológica",
            description = "Relación entre la diversidad cultural y la biología humana.",
            dateTime = LocalDateTime.of(2025, 11, 20, 12, 0)
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(): List<SymposiumModel> {
        return hardcodedSymposia.sortedBy { it.dateTime }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNextSymposiums(): List<SymposiumModel> {
        return simposiosProximos
    }

}