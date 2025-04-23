package com.ebcf.jnab.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.data.model.SpeakerModel
import com.ebcf.jnab.data.repository.SpeakerRepository

class SpeakersListViewModel : ViewModel() {

    private val repository = SpeakerRepository()

    private val _speakers = MutableLiveData<List<SpeakerModel>>().apply {
        value = repository.getAllSpeakers()
    }

    val speakers: LiveData<List<SpeakerModel>> = _speakers
}
