package com.ebcf.jnab

import com.ebcf.jnab.data.model.SymposiumEntity
import com.ebcf.jnab.data.repository.SymposiumRepositoryImpl
import com.ebcf.jnab.data.source.local.dao.SymposiumDao
import com.ebcf.jnab.data.source.local.mapper.toModel
import com.ebcf.jnab.data.source.local.mapper.toEntity
import com.ebcf.jnab.data.source.remote.FirestoreSymposiumDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class SymposiumRepositoryImplTest {

    private val dao = mockk<SymposiumDao>(relaxed = true)
    private val remoteDataSource = mockk<FirestoreSymposiumDataSource>(relaxed = true)
    private lateinit var repository: SymposiumRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    private val symposiumEntity = SymposiumEntity(
        id = 1,
        title = "Simposio 1",
        description = "Descripción 1",
        startDateTime = "2025-05-26T10:00:00",
        talksJson = "[]"
    )

    private val symposiumModel = symposiumEntity.toModel()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = SymposiumRepositoryImpl(dao, remoteDataSource)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAll returns mapped symposiums from DAO`() = runTest {
        coEvery { dao.getAll() } returns listOf(symposiumEntity)

        val result = repository.getAll()

        coVerify(exactly = 1) { dao.getAll() }
        assertEquals(1, result.size)
        assertEquals(symposiumModel, result.first())
    }

    @Test
    fun `insertAll maps models to entities and calls DAO insertAll`() = runTest {
        repository.insertAll(listOf(symposiumModel))

        coVerify(exactly = 1) { dao.insertAll(listOf(symposiumEntity)) }
    }

    @Test
    fun `update maps model to entity and calls DAO update`() = runTest {
        repository.update(symposiumModel)

        coVerify(exactly = 1) { dao.update(symposiumEntity) }
    }

    @Test
    fun `delete maps model to entity and calls DAO delete`() = runTest {
        repository.delete(symposiumModel)

        coVerify(exactly = 1) { dao.delete(symposiumEntity) }
    }

    @Test
    fun `getById returns mapped symposium from DAO`() = runTest {
        coEvery { dao.getById(1) } returns symposiumEntity

        val result = repository.getById(1)

        coVerify(exactly = 1) { dao.getById(1) }
        assertNotNull(result)
        assertEquals(symposiumModel, result)
    }

    @Test
    fun `getById returns null if DAO returns null`() = runTest {
        coEvery { dao.getById(2) } returns null

        val result = repository.getById(2)

        coVerify(exactly = 1) { dao.getById(2) }
        assertNull(result)
    }

    @Test
    fun `syncFromRemote fetches remote data and inserts to DAO`() = runTest {
        val remoteList = listOf(symposiumModel)
        coEvery { remoteDataSource.getAllSymposiums() } returns remoteList

        repository.syncFromRemote()

        coVerify(exactly = 1) { remoteDataSource.getAllSymposiums() }
        coVerify(exactly = 1) { dao.insertAll(remoteList.map { it.toEntity() }) }
    }
}