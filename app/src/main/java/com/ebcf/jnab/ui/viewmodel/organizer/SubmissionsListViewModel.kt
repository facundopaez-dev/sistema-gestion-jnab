package com.ebcf.jnab.ui.viewmodel.organizer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.domain.model.Submission
import com.ebcf.jnab.data.repository.SubmissionRepository

class SubmissionsListViewModel : ViewModel() {

    private val repository = SubmissionRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _submissions = MutableLiveData<List<Submission>>().apply {
        value = repository.getAll()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val submissions: LiveData<List<Submission>> = _submissions
}