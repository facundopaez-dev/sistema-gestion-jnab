package com.ebcf.jnab.data.source.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ebcf.jnab.domain.model.SymposiumModel
import com.ebcf.jnab.domain.model.TalkModel
import com.ebcf.jnab.data.common.SYMPOSIUMS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class FirestoreSymposiumDataSource(
    private val firestore: FirebaseFirestore
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllSymposiums(): List<SymposiumModel> {
        return try {
            val snapshot = firestore
                .collection(SYMPOSIUMS)
                .get()
                .await()

            Log.d("FirestoreSymposiumDataSource", "Documentos obtenidos: ${snapshot.size()}")

            snapshot.documents.mapNotNull { doc ->
                doc.toSymposiumModelOrNull()
            }

        } catch (e: Exception) {
            Log.e("FirestoreSymposiumDataSource", "Error al obtener simposios", e)
            emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun DocumentSnapshot.toSymposiumModelOrNull(): SymposiumModel? {
        return try {
            val id = getLong("id")?.toInt() ?: return null
            val title = getString("title") ?: return null
            val description = getString("description") ?: return null
            val startDateTime = LocalDateTime.parse(getString("startDateTime") ?: return null)

            val talks = get("talks")?.let {
                @Suppress("UNCHECKED_CAST")
                val rawList = it as? List<Map<String, Any>> ?: return null
                rawList.mapNotNull { talkMap -> talkMap.toTalkModelOrNull(id) }
            } ?: emptyList()

            SymposiumModel(
                id = id,
                title = title,
                description = description,
                startDateTime = startDateTime,
                talks = talks
            )
        } catch (e: Exception) {
            Log.e("FirestoreSymposiumDataSource", "Error en DocumentSnapshot.toSymposiumModelOrNull()", e)
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Map<String, Any>.toTalkModelOrNull(symposiumId: Int): TalkModel? {
        return try {
            TalkModel(
                id = (this["id"] as? Long)?.toInt() ?: return null,
                symposiumId = symposiumId,
                title = this["title"] as? String ?: return null,
                description = this["description"] as? String ?: return null,
                speakerId = (this["speakerId"] as? Long)?.toInt() ?: return null,
                date = LocalDate.parse(this["date"] as? String ?: return null),
                startTime = LocalTime.parse(this["startTime"] as? String ?: return null),
                endTime = LocalTime.parse(this["endTime"] as? String ?: return null)
            )
        } catch (e: Exception) {
            Log.e("FirestoreSymposiumDataSource", "Error en toTalkModelOrNull()", e)
            null
        }
    }
}