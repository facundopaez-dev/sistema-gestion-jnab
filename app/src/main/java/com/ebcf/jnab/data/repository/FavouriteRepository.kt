package com.ebcf.jnab.data.repository

import com.ebcf.jnab.data.model.FavouriteEntity
import com.ebcf.jnab.data.source.local.dao.FavouriteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavouriteRepository(
    private val dao: FavouriteDao
) {

    suspend fun addToFavourites(favourite: FavouriteEntity) = withContext(Dispatchers.IO) {
        dao.insert(favourite)
    }

    suspend fun removeFromFavourites(favourite: FavouriteEntity) = withContext(Dispatchers.IO) {
        dao.delete(favourite)
    }

    suspend fun getAllFavourites(): List<FavouriteEntity> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    suspend fun clearAllFavourites() = withContext(Dispatchers.IO) {
        dao.deleteAll()
    }
}
