package com.ebcf.jnab.data.model

data class TalkModel (
    val id: Number,
    val title: String,
    val description: String,
    val author: String //TODO: luego lo cambiamos por un objeto Speaker.
)