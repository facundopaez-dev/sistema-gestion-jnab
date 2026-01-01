package com.ebcf.jnab.ui.login

import androidx.lifecycle.ViewModel
import com.ebcf.jnab.data.repository.AuthRepositoryImpl
import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource
import com.ebcf.jnab.domain.model.AuthError
import com.ebcf.jnab.domain.repository.AuthRepository
import com.ebcf.jnab.utils.SingleLiveEvent

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl(FirebaseAuthRemoteDataSource.getInstance())
) : ViewModel() {

    val loginSuccess = SingleLiveEvent<String>()
    val loginError = SingleLiveEvent<AuthError>()

    fun login(email: String, password: String) {
        authRepository.login(
            email,
            password,
            onSuccess = { role ->
                loginSuccess.postValue(role)
            },
            onError = { error ->
                loginError.postValue(error)
            }
        )
    }
}