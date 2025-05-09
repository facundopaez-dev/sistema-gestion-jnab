package com.ebcf.jnab.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.data.repository.TalkRepository
import com.ebcf.jnab.data.model.TalkModel

class TalksListViewModel : ViewModel() {

    private val repository = TalkRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _talks = MutableLiveData<List<TalkModel>>().apply {
        value = repository.getAll(2) // TODO: Hardcoded
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val talks: LiveData<List<TalkModel>> = _talks

    private val _favoriteIds = MutableLiveData<Set<Int>>(setOf())
    val favoriteIds: LiveData<Set<Int>> get() = _favoriteIds

    fun toggleFavorite(talkId: Int) {
        val current = _favoriteIds.value ?: setOf()
        _favoriteIds.value = if (talkId in current) {
            current - talkId
        } else {
            current + talkId
        }
    }
}