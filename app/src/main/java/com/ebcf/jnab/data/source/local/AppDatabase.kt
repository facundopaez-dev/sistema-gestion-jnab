package com.ebcf.jnab.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ebcf.jnab.data.model.SymposiumEntity
import com.ebcf.jnab.data.source.local.dao.SymposiumDao

@Database(
    entities = [SymposiumEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun symposiumDao(): SymposiumDao
}