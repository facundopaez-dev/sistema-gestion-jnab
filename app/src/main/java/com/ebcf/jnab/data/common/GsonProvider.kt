package com.ebcf.jnab.data.common

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.lang.reflect.Type

@RequiresApi(Build.VERSION_CODES.O)
object GsonProvider {
    val instance: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
            .registerTypeAdapter(LocalDate::class.java, object : JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
                private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

                override fun deserialize(
                    json: JsonElement?,
                    typeOfT: Type?,
                    context: JsonDeserializationContext?
                ): LocalDate {
                    return LocalDate.parse(json?.asString, formatter)
                }

                override fun serialize(
                    src: LocalDate?,
                    typeOfSrc: Type?,
                    context: JsonSerializationContext?
                ): JsonElement {
                    return JsonPrimitive(src?.format(formatter))
                }
            })
            .create()
    }
}