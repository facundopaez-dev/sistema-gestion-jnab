package com.ebcf.jnab.ui.submission.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ebcf.jnab.data.repository.SubmissionRepository

class SubmissionDetailViewModelFactory(
    private val repository: SubmissionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubmissionDetailViewModel::class.java)) {
            return SubmissionDetailViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}