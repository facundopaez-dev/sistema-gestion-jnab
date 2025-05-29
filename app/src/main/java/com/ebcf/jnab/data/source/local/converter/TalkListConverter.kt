package com.ebcf.jnab.data.source.local.converter

import androidx.room.TypeConverter
import com.ebcf.jnab.domain.model.TalkModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TalkListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromTalkList(list: List<TalkModel>?): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toTalkList(json: String): List<TalkModel> {
        val type = object : TypeToken<List<TalkModel>>() {}.type
        return gson.fromJson(json, type)
    }
}