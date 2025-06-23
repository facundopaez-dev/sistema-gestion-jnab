package com.ebcf.jnab.domain.model

import java.time.LocalDateTime

data class Submission(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val status: SubmissionStatus,
    val submittedAt: LocalDateTime,
    val rejectionReason: String? = null // Campo opcional
)