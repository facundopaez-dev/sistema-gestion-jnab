package com.ebcf.jnab.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.ebcf.jnab.domain.model.RejectionReason
import com.ebcf.jnab.domain.model.Submission
import com.ebcf.jnab.domain.model.SubmissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
object SubmissionRepository {

    private val _hardcodedSubmissions = mutableListOf(
        Submission(
            id = "1",
            title = "Antropología Biológica",
            author = "Juan Pérez",
            description = "Estudio sobre genética humana",
            status = SubmissionStatus.APPROVED,
            submittedAt = LocalDateTime.of(2025, 4, 22, 11, 0)
        ),
        Submission(
            id = "2",
            title = "Antropología Cultural",
            author = "Ana Torres",
            description = "Estudio sobre prácticas culturales",
            status = SubmissionStatus.PENDING,
            submittedAt = LocalDateTime.of(2025, 5, 5, 14, 30)
        ),
        Submission(
            id = "3",
            title = "Primatologia",
            author = "Carlos Gómez",
            description = "Investigación sobre los primates",
            status = SubmissionStatus.REJECTED,
            submittedAt = LocalDateTime.of(2025, 4, 15, 9, 15),
            rejectionReason = RejectionReason.INCOMPLETE_SUBMISSION.toString()
        ),
        Submission(
            id = "4",
            title = "Paleontología Humana",
            author = "Luisa Ramírez",
            description = "Análisis de fósiles humanos antiguos",
            status = SubmissionStatus.APPROVED,
            submittedAt = LocalDateTime.of(2025, 3, 12, 16, 45)
        ),
        Submission(
            id = "5",
            title = "Arqueología Biológica",
            author = "Roberto Díaz",
            description = "Estudio sobre restos óseos antiguos",
            status = SubmissionStatus.PENDING,
            submittedAt = LocalDateTime.of(2025, 5, 8, 10, 0)
        ),
        Submission(
            id = "6",
            title = "Genética Evolutiva",
            author = "Sofía Martínez",
            description = "Investigación sobre el ADN y su evolución",
            status = SubmissionStatus.APPROVED,
            submittedAt = LocalDateTime.of(2025, 4, 18, 13, 20)
        )
    )

    private val _submissionsFlow = MutableStateFlow<List<Submission>>(listOf())
    val submissionsFlow: StateFlow<List<Submission>> = _submissionsFlow.asStateFlow()

    init {
        _submissionsFlow.value = _hardcodedSubmissions.sortedBy { it.submittedAt }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getById(id: String): Submission {
        return _hardcodedSubmissions.find { it.id == id }
            ?: throw NoSuchElementException("Trabajo con ID $id no encontrado")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun approve(id: String): Submission {
        return updateStatus(id, SubmissionStatus.APPROVED, rejectionReason = null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun reject(id: String, reason: String): Submission {
        return updateStatus(id, SubmissionStatus.REJECTED, rejectionReason = reason)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStatus(
        id: String,
        status: SubmissionStatus,
        rejectionReason: String? = null
    ): Submission {
        val index = _hardcodedSubmissions.indexOfFirst { it.id == id }
        if (index == -1) throw NoSuchElementException("Trabajo con ID $id no encontrado")

        val original = _hardcodedSubmissions[index]
        val updated = original.copy(
            status = status,
            rejectionReason = rejectionReason
        )

        _hardcodedSubmissions[index] = updated
        _submissionsFlow.value = _hardcodedSubmissions.sortedBy { it.submittedAt }

        return updated
    }
}