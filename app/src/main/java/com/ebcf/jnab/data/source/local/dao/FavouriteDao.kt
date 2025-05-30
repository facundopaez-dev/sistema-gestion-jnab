package com.ebcf.jnab.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ebcf.jnab.data.model.FavouriteEntity

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favourite: FavouriteEntity)

    @Delete
    suspend fun delete(favourite: FavouriteEntity)

    @Query("SELECT * FROM favourites")
    suspend fun getAll(): List<FavouriteEntity>

    @Query("DELETE FROM favourites")
    suspend fun deleteAll()
}
