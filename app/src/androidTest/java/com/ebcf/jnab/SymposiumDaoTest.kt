package com.ebcf.jnab

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ebcf.jnab.data.model.SymposiumEntity
import com.ebcf.jnab.data.source.local.AppDatabase
import com.ebcf.jnab.data.source.local.dao.SymposiumDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SymposiumDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: SymposiumDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = database.symposiumDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetAllSymposiums() = runBlocking {
        val symposium = SymposiumEntity(
            id = 1,
            title = "Simposio 1",
            description = "Descripción 1",
            startDateTime = "2025-05-26T10:00:00",
            talksJson = "[]"
        )
        dao.insert(symposium)
        val all = dao.getAll()
        assertEquals(1, all.size)
        assertEquals("Simposio 1", all[0].title)
    }

    @Test
    fun insertAllAndGetAllSymposiums() = runBlocking {
        val symposiums = listOf(
            SymposiumEntity(2, "Simposio 2", "Desc 2", "2025-05-26T13:00:00", "[]"),
            SymposiumEntity(3, "Simposio 3", "Desc 3", "2025-05-26T16:00:00", "[]")
        )
        dao.insertAll(symposiums)
        val all = dao.getAll()
        assertEquals(2, all.size)
        assertTrue(all.any { it.title == "Simposio 2" })
        assertTrue(all.any { it.title == "Simposio 3" })
    }

    @Test
    fun updateSymposium() = runBlocking {
        val symposium = SymposiumEntity(
            id = 4,
            title = "Simposio 4",
            description = "Desc 4",
            startDateTime = "2025-05-26T19:00:00",
            talksJson = "[]"
        )
        dao.insert(symposium)

        val updatedSymposium = symposium.copy(title = "Simposio 4 Modificado")
        dao.update(updatedSymposium)

        val fetched = dao.getById(4)
        assertNotNull(fetched)
        assertEquals("Simposio 4 Modificado", fetched?.title)
    }

    @Test
    fun deleteSymposium() = runBlocking {
        val symposium = SymposiumEntity(
            id = 5,
            title = "Simposio 5",
            description = "Desc 5",
            startDateTime = "2025-05-26T22:00:00",
            talksJson = "[]"
        )
        dao.insert(symposium)
        dao.delete(symposium)
        val fetched = dao.getById(5)
        assertNull(fetched)
    }

    @Test
    fun deleteAllSymposiums() = runBlocking {
        val symposiums = listOf(
            SymposiumEntity(
                6,
                "Simposio 6",
                "Desc 6",
                "2025-05-27T01:00:00",
                "[]"
            ),
            SymposiumEntity(
                7,
                "Simposio 7",
                "Desc 7",
                "2025-05-27T04:00:00",
                "[]"
            )
        )
        dao.insertAll(symposiums)
        dao.deleteAll()
        val all = dao.getAll()
        assertTrue(all.isEmpty())
    }

    @Test
    fun getByIdSymposium() = runBlocking {
        val symposium = SymposiumEntity(
            id = 8,
            title = "Simposio 8",
            description = "Desc 8",
            startDateTime = "2025-05-27T07:00:00",
            talksJson = "[]"
        )
        dao.insert(symposium)
        val fetched = dao.getById(8)
        assertNotNull(fetched)
        assertEquals("Simposio 8", fetched?.title)
    }
}