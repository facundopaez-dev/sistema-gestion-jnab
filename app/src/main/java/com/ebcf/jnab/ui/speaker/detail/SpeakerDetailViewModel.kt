package com.ebcf.jnab.ui.speaker.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.domain.model.SpeakerModel
import com.ebcf.jnab.data.repository.SpeakerRepository

class SpeakerDetailViewModel : ViewModel() {

    private val repository = SpeakerRepository()

    private val _speaker = MutableLiveData<SpeakerModel>()
    val speaker: LiveData<SpeakerModel> = _speaker

    fun loadSpeakerById(id: Int) {
        val result = repository.getSpeakerById(id)
        result?.let {
            _speaker.value = it
        }
    }
}
