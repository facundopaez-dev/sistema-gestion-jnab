package com.ebcf.jnab.domain.repository

import com.ebcf.jnab.domain.model.SymposiumModel

interface SymposiumRepository {
    suspend fun getAll(): List<SymposiumModel>
    suspend fun insertAll(symposiums: List<SymposiumModel>): List<Long>
    suspend fun update(symposium: SymposiumModel)
    suspend fun delete(symposium: SymposiumModel)
    suspend fun getById(id: Int): SymposiumModel?
    suspend fun syncFromRemote()
}