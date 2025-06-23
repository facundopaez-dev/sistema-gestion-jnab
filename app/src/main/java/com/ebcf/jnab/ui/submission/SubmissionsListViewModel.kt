package com.ebcf.jnab.ui.submission

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.ebcf.jnab.data.repository.SubmissionRepository
import com.ebcf.jnab.domain.model.Submission
import kotlinx.coroutines.flow.StateFlow

class SubmissionsListViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    val submissionsFlow: StateFlow<List<Submission>> = SubmissionRepository.submissionsFlow
}