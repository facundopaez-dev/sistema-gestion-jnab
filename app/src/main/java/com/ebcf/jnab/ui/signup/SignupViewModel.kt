package com.ebcf.jnab.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebcf.jnab.domain.repository.SignupRepository
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.launch

class SignupViewModel(
    private val repository: SignupRepository
) : ViewModel() {

    private val _signupState = MutableLiveData<SignupState>()
    val signupState: LiveData<SignupState> = _signupState

    fun signup(email: String, password: String, firstName: String, lastName: String) {
        _signupState.value = SignupState.Loading

        viewModelScope.launch {
            try {
                repository.signup(email, password, firstName, lastName)
                _signupState.value = SignupState.Result(SignupMessage.SignupSuccess)
            } catch (e: Exception) {
                val message = if (e is FirebaseAuthUserCollisionException) {
                    SignupMessage.EmailAlreadyRegistered
                } else {
                    SignupMessage.SignupError
                }
                _signupState.value = SignupState.Result(message)
            }
        }
    }
}