package com.ebcf.jnab.data.model

enum class UserRole(val value: String) {
    ASSISTANT("assistant"),
    SPEAKER("speaker"),
    ORGANIZER("organizer");

    companion object {
        fun fromValue(value: String): UserRole? {
            return values().find { it.value == value }
        }
    }
}
