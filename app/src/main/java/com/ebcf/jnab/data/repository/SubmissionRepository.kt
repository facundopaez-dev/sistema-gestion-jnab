package com.ebcf.jnab.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ebcf.jnab.domain.model.Submission
import com.ebcf.jnab.domain.model.SubmissionStatus
import java.time.LocalDateTime

class SubmissionRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    val hardcodedSubmissions = listOf(
        Submission(
            title = "Antropología Biológica",
            author = "Juan Pérez",
            description = "Estudio sobre genética humana",
            status = SubmissionStatus.APPROVED,
            submittedAt = LocalDateTime.of(2025, 4, 22, 11, 0)
        ),
        Submission(
            title = "Antropología Cultural",
            author = "Ana Torres",
            description = "Estudio sobre prácticas culturales",
            status = SubmissionStatus.PENDING,
            submittedAt = LocalDateTime.of(2025, 5, 5, 14, 30)
        ),
        Submission(
            title = "Primatologia",
            author = "Carlos Gómez",
            description = "Investigación sobre los primates",
            status = SubmissionStatus.REJECTED,
            submittedAt = LocalDateTime.of(2025, 4, 15, 9, 15)
        ),
        Submission(
            title = "Paleontología Humana",
            author = "Luisa Ramírez",
            description = "Análisis de fósiles humanos antiguos",
            status = SubmissionStatus.APPROVED,
            submittedAt = LocalDateTime.of(2025, 3, 12, 16, 45)
        ),
        Submission(
            title = "Arqueología Biológica",
            author = "Roberto Díaz",
            description = "Estudio sobre restos óseos antiguos",
            status = SubmissionStatus.PENDING,
            submittedAt = LocalDateTime.of(2025, 5, 8, 10, 0)
        ),
        Submission(
            title = "Genética Evolutiva",
            author = "Sofía Martínez",
            description = "Investigación sobre el ADN y su evolución",
            status = SubmissionStatus.APPROVED,
            submittedAt = LocalDateTime.of(2025, 4, 18, 13, 20)
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(): List<Submission> {
        return hardcodedSubmissions.sortedBy { it.submittedAt }
    }

}