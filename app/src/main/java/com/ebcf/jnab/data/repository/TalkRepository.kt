package com.ebcf.jnab.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ebcf.jnab.domain.model.TalkModel
import java.time.LocalDate
import java.time.LocalTime

class TalkRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    private val hardcodedTalks = listOf(
        TalkModel(
            id = 1,
            symposiumId = 1,
            title = "Identidades en movimiento: migración y cultura",
            description = "Un análisis de cómo las experiencias migratorias transforman las identidades culturales en contextos urbanos.",
            speakerId =1 ,
            date = LocalDate.of(2025, 4, 22),
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(10, 0)
        ),
        TalkModel(
            id = 2,
            symposiumId = 1,
            title = "Rituales ancestrales y su vigencia en la actualidad",
            description = "Exploración de prácticas rituales originarias que aún perduran y su resignificación en contextos contemporáneos.",
            speakerId =2 ,// "Dr. Martín Ruiz",
            date = LocalDate.of(2025, 4, 22),
            startTime = LocalTime.of(10, 15),
            endTime = LocalTime.of(11, 15)
        ),
        TalkModel(
            id = 3,
            symposiumId = 1,
            title = "Antropología del cuerpo: género y simbolismo",
            description = "Reflexiones sobre el cuerpo como construcción social y simbólica en distintas culturas.",
            speakerId =2 ,// "Ana Lucía Paredes",
            date = LocalDate.of(2025, 4, 23),
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(10, 0)
        ),
        TalkModel(
            id = 4,
            symposiumId = 2,
            title = "Memoria colectiva y narrativas del pasado",
            description = "Estudio sobre cómo las comunidades reconstruyen el pasado a través de la oralidad y la práctica social.",
            speakerId =1 ,// "Javier Méndez",
            date = LocalDate.of(2025, 4, 23),
            startTime = LocalTime.of(10, 15),
            endTime = LocalTime.of(11, 15)
        ),
        TalkModel(
            id = 5,
            symposiumId = 1,
            title = "Territorio y resistencia en comunidades indígenas",
            description = "Una mirada a los procesos de defensa territorial y cultural frente a políticas extractivistas.",
            speakerId =1 ,// "Mariela Quispe",
            date = LocalDate.of(2025, 4, 23),
            startTime = LocalTime.of(14, 0),
            endTime = LocalTime.of(15, 0)
        ),
        TalkModel(
            id = 6,
            symposiumId = 1,
            title = "Etnografías digitales: nuevos campos de estudio",
            description = "Aportes metodológicos y éticos en la investigación antropológica en entornos virtuales.",
            speakerId =1 ,// "Sofía Navarro",
            date = LocalDate.of(2025, 4, 24),
            startTime = LocalTime.of(11, 30),
            endTime = LocalTime.of(12, 30)
        ),
        TalkModel(
            id = 7,
            symposiumId = 2,
            title = "La alimentación como hecho cultural",
            description = "Análisis de prácticas alimentarias y su relación con la identidad, el poder y la globalización.",
            speakerId =1, // "Pablo Gómez",
            date = LocalDate.of(2025, 4, 25),
            startTime = LocalTime.of(13, 0),
            endTime = LocalTime.of(14, 0)
        ),
        TalkModel(
            id = 8,
            symposiumId = 2,
            title = "Cosmovisiones y prácticas medicinales tradicionales",
            description = "Estudio de los saberes médicos ancestrales y su articulación con el sistema biomédico.",
            speakerId =2 ,// "Elena Vargas",
            date = LocalDate.of(2025, 4, 25),
            startTime = LocalTime.of(14, 15),
            endTime = LocalTime.of(15, 15)
        )

    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(id: Number): List<TalkModel> {
        val now = LocalTime.now()
        val today = LocalDate.now()

        val testTalk = TalkModel(
            id = 999,
            symposiumId = 1,
            title = "Simulación de charla para probar la notificación",
            description = "",
            speakerId = 1,
            date = today,
            startTime = now.plusHours(1).withSecond(0).withNano(0),
            endTime = now.plusHours(2).withSecond(0).withNano(0)
        )

        return listOf(testTalk) + hardcodedTalks
    }

}
