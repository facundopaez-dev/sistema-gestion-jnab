package com.ebcf.jnab.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ebcf.jnab.data.model.SymposiumEntity
import com.ebcf.jnab.data.model.FavouriteEntity
import com.ebcf.jnab.data.source.local.dao.FavouriteDao
import com.ebcf.jnab.data.source.local.dao.SymposiumDao

@Database(
    entities = [SymposiumEntity::class, FavouriteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun symposiumDao(): SymposiumDao
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also { instance = it }
            }
    }
}