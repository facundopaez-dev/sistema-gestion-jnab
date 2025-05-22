package com.ebcf.jnab.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.data.repository.TalkRepository
import com.ebcf.jnab.domain.model.TalkModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class TalksListViewModel : ViewModel() {


    private val repository = TalkRepository()
    private var allTalks: List<TalkModel> = listOf()

    private val _talks = MutableLiveData<List<TalkModel>>()
    val talks: LiveData<List<TalkModel>> get() = _talks

    private val _favoriteIds = MutableLiveData<Set<Int>>(setOf())
    val favoriteIds: LiveData<Set<Int>> get() = _favoriteIds

    private val _filteredTalks = MutableLiveData<List<TalkModel>>()
    val filteredTalks: LiveData<List<TalkModel>> = _filteredTalks

    init {
        loadTalks()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadTalks() {
        allTalks = repository.getAll(2) // TODO: Hardcoded
        _talks.value = allTalks
    }

    fun toggleFavorite(talkId: Int) {
        val current = _favoriteIds.value ?: setOf()
        _favoriteIds.value = if (talkId in current) {
            current - talkId
        } else {
            current + talkId
        }
    }


    fun applyFilters(symposiumId: Int?, speakerId: Int?, date: LocalDate?) {
        Log.d("TalksListViewModel", "Applying filters: SymposiumId=$symposiumId, SpeakerId=$speakerId, Date=$date")
        val allTalks = _talks.value ?: emptyList()

        val filtered = allTalks.filter { talk ->
            (symposiumId == null || talk.symposiumId == symposiumId) &&
                    (speakerId == null || talk.speakerId == speakerId) &&
                    (date == null || talk.date.isEqual(date))
        }

        _filteredTalks.value = filtered
        Log.d("adsf","$filtered")

    }

    fun clearFilters() {
        _filteredTalks.value = _talks.value
    }


}