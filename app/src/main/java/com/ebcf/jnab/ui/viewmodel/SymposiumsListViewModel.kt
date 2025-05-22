package com.ebcf.jnab.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.domain.model.SymposiumModel
import com.ebcf.jnab.data.repository.SymposiumRepository

class SymposiumsListViewModel : ViewModel() {

    private val repository = SymposiumRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _symposiums = MutableLiveData<List<SymposiumModel>>().apply {
        value = repository.getAll()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val symposiums: LiveData<List<SymposiumModel>> = _symposiums


    @RequiresApi(Build.VERSION_CODES.O)
    fun getSymposiumById(id: Int): SymposiumModel? {
        return _symposiums.value?.firstOrNull { it.id == id }
    }

}