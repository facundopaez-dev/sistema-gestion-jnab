package com.ebcf.jnab.data.common

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalTime
import java.time.format.DateTimeParseException

class LocalTimeAdapter : TypeAdapter<LocalTime>() {

    override fun write(out: JsonWriter, value: LocalTime?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value.toString()) // ISO_LOCAL_TIME formato "HH:mm:ss"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun read(reader: JsonReader): LocalTime? {
        return try {
            val timeString = reader.nextString()
            LocalTime.parse(timeString) // parsea de formato ISO "HH:mm:ss"
        } catch (e: DateTimeParseException) {
            Log.e("LocalTimeAdapter", "Error al parsear la hora: ${e.message}", e)
            null
        }
    }
}