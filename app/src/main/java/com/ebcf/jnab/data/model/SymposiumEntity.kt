package com.ebcf.jnab.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ebcf.jnab.data.common.SYMPOSIUMS

@Entity(
    tableName = SYMPOSIUMS,
    indices = [Index(value = ["title", "startDateTime"], unique = true)]
)
data class SymposiumEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val startDateTime: String, // se convertira desde LocalDateTime
    val talksJson: String      // se convertira desde List<TalkModel>
)