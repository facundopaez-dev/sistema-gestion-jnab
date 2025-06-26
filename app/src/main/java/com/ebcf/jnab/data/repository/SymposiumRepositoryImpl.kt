package com.ebcf.jnab.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ebcf.jnab.data.source.local.dao.SymposiumDao
import com.ebcf.jnab.data.source.local.mapper.toEntity
import com.ebcf.jnab.data.source.local.mapper.toModel
import com.ebcf.jnab.data.source.remote.FirestoreSymposiumDataSource
import com.ebcf.jnab.domain.model.SymposiumModel
import com.ebcf.jnab.domain.repository.SymposiumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SymposiumRepositoryImpl(
    private val dao: SymposiumDao,
    private val remoteDataSource: FirestoreSymposiumDataSource
) : SymposiumRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAll(): List<SymposiumModel> = withContext(Dispatchers.IO) {
        dao.getAll().map { it.toModel() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun insertAll(symposia: List<SymposiumModel>) = withContext(Dispatchers.IO) {
        dao.insertAll(symposia.map { it.toEntity() })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun update(symposium: SymposiumModel) = withContext(Dispatchers.IO) {
        dao.update(symposium.toEntity())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun delete(symposium: SymposiumModel) = withContext(Dispatchers.IO) {
        dao.delete(symposium.toEntity())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getById(id: Int): SymposiumModel? = withContext(Dispatchers.IO) {
        dao.getById(id)?.toModel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun syncFromRemote() {
        withContext(Dispatchers.IO) {
            val remoteSymposiums = remoteDataSource.getAllSymposiums()
            Log.d(
                "DefaultSymposiumRepository",
                "Simbolos obtenidos de Firestore: ${remoteSymposiums.size}"
            )

            val insertedIds = dao.insertAll(remoteSymposiums.map { it.toEntity() })
            Log.d("DefaultSymposiumRepository", "Simbolos insertados en Room: ${insertedIds.size}")
        }
    }
}