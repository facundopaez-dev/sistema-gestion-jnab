package com.ebcf.jnab.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.data.repository.TalkRepository
import com.ebcf.jnab.data.model.TalkModel

class TalksListViewModel : ViewModel() {

    private val repository = TalkRepository()

    private val _talks = MutableLiveData<List<TalkModel>>().apply {
        value = repository.getAll(2) // TODO: Hardcoded
    }

    val talks: LiveData<List<TalkModel>> = _talks
}