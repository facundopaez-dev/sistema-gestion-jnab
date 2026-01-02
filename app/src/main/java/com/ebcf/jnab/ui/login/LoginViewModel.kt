package com.ebcf.jnab.ui.login

import androidx.lifecycle.ViewModel
import com.ebcf.jnab.data.repository.AuthRepositoryImpl
import com.ebcf.jnab.data.source.remote.FirebaseAuthRemoteDataSource
import com.ebcf.jnab.domain.model.AuthError
import com.ebcf.jnab.domain.model.LoginResult
import com.ebcf.jnab.domain.model.User
import com.ebcf.jnab.domain.repository.AuthRepository
import com.ebcf.jnab.utils.SingleLiveEvent

class LoginViewModel(
    private val authRepository: AuthRepository =
        AuthRepositoryImpl(FirebaseAuthRemoteDataSource.getInstance())
) : ViewModel() {

    val loginSuccess = SingleLiveEvent<User>()
    val loginError = SingleLiveEvent<AuthError>()

    fun login(email: String, password: String) {
        authRepository.login(email, password) { result ->
            when (result) {
                is LoginResult.Success -> {
                    loginSuccess.postValue(result.user)
                }

                is LoginResult.Error -> {
                    loginError.postValue(result.error)
                }
            }
        }
    }
}