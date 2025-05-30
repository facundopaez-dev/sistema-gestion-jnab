package com.ebcf.jnab.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ebcf.jnab.data.common.FAVOURITES

@Entity(tableName = FAVOURITES)
data class FavouriteEntity(
    @PrimaryKey val talkId: Int,
)