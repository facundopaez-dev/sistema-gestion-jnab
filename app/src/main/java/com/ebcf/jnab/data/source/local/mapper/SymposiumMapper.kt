package com.ebcf.jnab.data.source.local.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import com.ebcf.jnab.data.model.SymposiumEntity
import com.ebcf.jnab.domain.model.SymposiumModel
import com.ebcf.jnab.domain.model.TalkModel
import com.ebcf.jnab.data.common.GsonProvider
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun SymposiumEntity.toModel(): SymposiumModel {
    return SymposiumModel(
        id = id,
        title = title,
        description = description,
        startDateTime = LocalDateTime.parse(startDateTime),
        talks = GsonProvider.instance.fromJson(talksJson, object : TypeToken<List<TalkModel>>() {}.type)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun SymposiumModel.toEntity(): SymposiumEntity {
    return SymposiumEntity(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        talksJson = GsonProvider.instance.toJson(talks)
    )
}