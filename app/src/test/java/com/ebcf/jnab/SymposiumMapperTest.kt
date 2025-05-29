package com.ebcf.jnab

import com.google.gson.reflect.TypeToken
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime.of
import java.time.format.DateTimeFormatter
import com.ebcf.jnab.data.common.GsonProvider
import com.ebcf.jnab.data.model.SymposiumEntity
import com.ebcf.jnab.data.source.local.mapper.toEntity
import com.ebcf.jnab.data.source.local.mapper.toModel
import com.ebcf.jnab.domain.model.SymposiumModel
import com.ebcf.jnab.domain.model.TalkModel

class SymposiumMapperTest {

    private val gson = GsonProvider.instance

    @Test
    fun testToModel() {
        val talksList = listOf(
            TalkModel(1, 1, "Talk 1", "Desc 1", 10, LocalDate.of(2025, 5, 27), of(10, 0), of(11, 0))
        )
        val talksJson = gson.toJson(talksList)

        val entity = SymposiumEntity(
            id = 1,
            title = "Symposium 1",
            description = "Description 1",
            startDateTime = "2025-05-27T09:00:00",
            talksJson = talksJson
        )

        val model = entity.toModel()

        assertEquals(entity.id, model.id)
        assertEquals(entity.title, model.title)
        assertEquals(entity.description, model.description)
        assertEquals(LocalDateTime.parse(entity.startDateTime), model.startDateTime)
        assertEquals(talksList.size, model.talks.size)
        assertEquals(talksList[0].title, model.talks[0].title)
    }

    @Test
    fun testToEntity() {
        val talksList = listOf(
            TalkModel(1, 1, "Talk 1", "Desc 1", 10, LocalDate.of(2025, 5, 27), of(10, 0), of(11, 0))
        )

        val model = SymposiumModel(
            id = 1,
            title = "Symposium 1",
            description = "Description 1",
            startDateTime = LocalDateTime.of(2025, 5, 27, 9, 0),
            talks = talksList
        )

        val entity = model.toEntity()

        assertEquals(model.id, entity.id)
        assertEquals(model.title, entity.title)
        assertEquals(model.description, entity.description)
        assertEquals(
            model.startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            entity.startDateTime
        )

        val talksFromJson: List<TalkModel> =
            gson.fromJson(entity.talksJson, object : TypeToken<List<TalkModel>>() {}.type)

        assertEquals(talksList.size, talksFromJson.size)
        assertEquals(talksList[0].title, talksFromJson[0].title)
    }
}