package com.ebcf.jnab.data.common

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value.format(formatter))  // Ejemplo: 2023-08-17T15:30:00
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun read(reader: JsonReader): LocalDateTime? {
        return try {
            val dateTimeString = reader.nextString()
            LocalDateTime.parse(dateTimeString, formatter)
        } catch (e: DateTimeParseException) {
            Log.e("LocalDateTimeAdapter", "Error al parsear fecha y hora: ${e.message}", e)
            null
        }
    }
}