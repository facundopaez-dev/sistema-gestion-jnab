package com.ebcf.jnab.ui.submission.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebcf.jnab.R
import com.ebcf.jnab.data.repository.SubmissionRepository
import com.ebcf.jnab.domain.model.Submission
import com.ebcf.jnab.domain.model.SubmissionStatus
import kotlinx.coroutines.launch

class SubmissionDetailViewModel(private val repository: SubmissionRepository) : ViewModel() {

    private val _submission = MutableLiveData<Submission>()
    val submission: LiveData<Submission> = _submission

    private val _statusColorRes = MutableLiveData<Int>()
    val statusColorRes: LiveData<Int> = _statusColorRes

    private val _showActionButtons = MutableLiveData<Boolean>()
    val showActionButtons: LiveData<Boolean> = _showActionButtons

    fun updateShowButtons(status: SubmissionStatus) {
        _showActionButtons.value = (status == SubmissionStatus.PENDING)
    }

    fun updateStatusColor(status: SubmissionStatus) {
        _statusColorRes.value = when (status) {
            SubmissionStatus.APPROVED -> R.color.submission_status_approved
            SubmissionStatus.REJECTED -> R.color.submission_status_rejected
            SubmissionStatus.PENDING -> R.color.submission_status_pending
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun load(id: String) {
        viewModelScope.launch {
            val result = repository.getById(id)
            _submission.value = result
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun approve(id: String) {
        viewModelScope.launch {
            val updated = repository.approve(id)
            _submission.value = updated
            updateStatusColor(updated.status)
            updateShowButtons(updated.status)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun undoApprove(submissionId: String) {
        // "Pendiente" es el estado previo antes de aprobar o rechazar
        val reverted = repository.updateStatus(
            submissionId,
            SubmissionStatus.PENDING,
            rejectionReason = null
        )
        _submission.value = reverted
        updateStatusColor(reverted.status)
        updateShowButtons(reverted.status)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun reject(id: String, reason: String) {
        viewModelScope.launch {
            val updated = repository.reject(id, reason)
            _submission.value = updated
            updateStatusColor(updated.status)
            updateShowButtons(updated.status)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun undoReject(submissionId: String) {
        viewModelScope.launch {
            // "Pendiente" es el estado previo antes de aprobar o rechazar
            val reverted = repository.updateStatus(
                submissionId,
                SubmissionStatus.PENDING,
                rejectionReason = null
            )
            _submission.value = reverted
            updateStatusColor(reverted.status)
            updateShowButtons(reverted.status)
        }
    }

}