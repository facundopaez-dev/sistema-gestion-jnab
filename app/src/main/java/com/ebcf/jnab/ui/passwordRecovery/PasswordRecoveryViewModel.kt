package com.ebcf.jnab.ui.passwordRecovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.domain.repository.AuthRepository

class PasswordRecoveryViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    sealed class PasswordRecoveryState {
        object Loading : PasswordRecoveryState()
        object Success : PasswordRecoveryState()
        data class Error(val exception: Exception) : PasswordRecoveryState()
    }

    private val _state = MutableLiveData<PasswordRecoveryState>()
    val state: LiveData<PasswordRecoveryState> get() = _state

    fun sendPasswordResetEmail(email: String) {
        _state.value = PasswordRecoveryState.Loading

        authRepository.sendPasswordResetEmail(
            email = email,
            onSuccess = {
                _state.value = PasswordRecoveryState.Success
            },
            onError = { exception ->
                _state.value = PasswordRecoveryState.Error(exception)
            }
        )
    }
}