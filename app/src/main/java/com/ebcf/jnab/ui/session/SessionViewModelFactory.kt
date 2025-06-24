package com.ebcf.jnab.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource

class SessionViewModelFactory(
    private val authDataSource: FirebaseAuthRemoteDataSource
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            return SessionViewModel(authDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}