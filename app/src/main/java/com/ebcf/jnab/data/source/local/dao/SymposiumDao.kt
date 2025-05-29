package com.ebcf.jnab.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ebcf.jnab.data.model.SymposiumEntity

@Dao
interface SymposiumDao {

    // Inserta el entity en la base de datos
    // La clave unica configurada en SymposiumEntity es la combinacion de title + startDateTime
    // Si ya existe un registro con esa clave unica, elimina el registro anterior y lo reemplaza por el nuevo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SymposiumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SymposiumEntity>): List<Long>

    @Update
    suspend fun update(entity: SymposiumEntity)

    @Delete
    suspend fun delete(entity: SymposiumEntity)

    @Query("DELETE FROM symposiums")
    suspend fun deleteAll()

    @Query("SELECT * FROM symposiums")
    suspend fun getAll(): List<SymposiumEntity>

    @Query("SELECT * FROM symposiums WHERE id = :id")
    suspend fun getById(id: Int): SymposiumEntity?
}