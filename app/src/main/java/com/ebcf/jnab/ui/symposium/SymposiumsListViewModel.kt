package com.ebcf.jnab.ui.symposium

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebcf.jnab.domain.model.SymposiumModel
import com.ebcf.jnab.domain.repository.SymposiumRepository
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class SymposiumsListViewModel(
    private val repository: SymposiumRepository
) : ViewModel() {

    private val _symposiums = MutableLiveData<List<SymposiumModel>>()
    val symposiums: LiveData<List<SymposiumModel>> = _symposiums

    init {
        viewModelScope.launch {
            repository.syncFromRemote() // Trae los simposios desde Firestore y los guarda en Room
            _symposiums.value = repository.getAll() // Obtiene los simposios desde Room
        }
    }

    fun getById(id: Int, onResult: (SymposiumModel?) -> Unit) {
        viewModelScope.launch {
            val symposium = repository.getById(id)
            onResult(symposium)
        }
    }
}