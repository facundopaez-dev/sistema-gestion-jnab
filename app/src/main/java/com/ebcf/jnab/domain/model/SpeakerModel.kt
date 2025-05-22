package com.ebcf.jnab.domain.model

data class SpeakerModel(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val institution: String,
    val email: String,
    val talks: List<TalkModel>
)