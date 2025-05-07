package com.ebcf.jnab.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ebcf.jnab.data.model.TalkModel
import java.time.LocalDateTime

class TalkRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    private val hardcodedTalks = listOf(
        TalkModel(
            id = 1,
            title = "Identidades en movimiento: migración y cultura",
            description = "Un análisis de cómo las experiencias migratorias transforman las identidades culturales en contextos urbanos.",
            author = "Laura Fernández",
            startDateTime = LocalDateTime.of(2025, 4, 22, 9, 0),
            endDateTime = LocalDateTime.of(2025, 4, 22, 10, 0)
        ),
        TalkModel(
            id = 2,
            title = "Rituales ancestrales y su vigencia en la actualidad",
            description = "Exploración de prácticas rituales originarias que aún perduran y su resignificación en contextos contemporáneos.",
            author = "Dr. Martín Ruiz",
            startDateTime = LocalDateTime.of(2025, 4, 22, 10, 15),
            endDateTime = LocalDateTime.of(2025, 4, 22, 11, 15)
        ),
        TalkModel(
            id = 3,
            title = "Antropología del cuerpo: género y simbolismo",
            description = "Reflexiones sobre el cuerpo como construcción social y simbólica en distintas culturas.",
            author = "Ana Lucía Paredes",
            startDateTime = LocalDateTime.of(2025, 4, 23, 9, 0),
            endDateTime = LocalDateTime.of(2025, 4, 23, 10, 0)
        ),
        TalkModel(
            id = 4,
            title = "Memoria colectiva y narrativas del pasado",
            description = "Estudio sobre cómo las comunidades reconstruyen el pasado a través de la oralidad y la práctica social.",
            author = "Javier Méndez",
            startDateTime = LocalDateTime.of(2025, 4, 23, 10, 15),
            endDateTime = LocalDateTime.of(2025, 4, 23, 11, 15)
        ),
        TalkModel(
            id = 5,
            title = "Territorio y resistencia en comunidades indígenas",
            description = "Una mirada a los procesos de defensa territorial y cultural frente a políticas extractivistas.",
            author = "Mariela Quispe",
            startDateTime = LocalDateTime.of(2025, 4, 23, 14, 0),
            endDateTime = LocalDateTime.of(2025, 4, 23, 15, 0)
        ),
        TalkModel(
            id = 6,
            title = "Etnografías digitales: nuevos campos de estudio",
            description = "Aportes metodológicos y éticos en la investigación antropológica en entornos virtuales.",
            author = "Sofía Navarro",
            startDateTime = LocalDateTime.of(2025, 4, 24, 11, 30),
            endDateTime = LocalDateTime.of(2025, 4, 24, 12, 30)
        ),
        TalkModel(
            id = 7,
            title = "La alimentación como hecho cultural",
            description = "Análisis de prácticas alimentarias y su relación con la identidad, el poder y la globalización.",
            author = "Pablo Gómez",
            startDateTime = LocalDateTime.of(2025, 4, 25, 13, 0),
            endDateTime = LocalDateTime.of(2025, 4, 25, 14, 0)
        ),
        TalkModel(
            id = 8,
            title = "Cosmovisiones y prácticas medicinales tradicionales",
            description = "Estudio de los saberes médicos ancestrales y su articulación con el sistema biomédico.",
            author = "Elena Vargas",
            startDateTime = LocalDateTime.of(2025, 4, 25, 14, 15),
            endDateTime = LocalDateTime.of(2025, 4, 25, 15, 15)
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(id: Number): List<TalkModel> {
        return hardcodedTalks
    }
}
