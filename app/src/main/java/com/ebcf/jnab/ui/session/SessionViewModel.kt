package com.ebcf.jnab.ui.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource

// SessionViewModel se encarga unicamente de gestionar el estado de la sesion iniciada
// (por ejemplo: cerrar sesion, verificar si la sesion esta activa, manejar el cierre global, etc.).
class SessionViewModel(private val authRepository: FirebaseAuthRemoteDataSource) : ViewModel() {

    private val _logoutEvent = MutableLiveData<Unit>()
    val logoutEvent: LiveData<Unit> get() = _logoutEvent

    fun logout() {
        authRepository.logout()
        _logoutEvent.value = Unit // Notifica a la UI que se cerro la sesion
    }
}