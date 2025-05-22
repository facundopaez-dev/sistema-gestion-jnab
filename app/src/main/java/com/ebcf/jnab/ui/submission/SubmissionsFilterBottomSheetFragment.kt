package com.ebcf.jnab.ui.submission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ebcf.jnab.domain.model.SubmissionStatus
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.FragmentSubmissionsFilterBottomSheetBinding

class SubmissionsFilterBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSubmissionsFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    var onFilterSelected: ((SubmissionStatus?) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubmissionsFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnApplyFilter.setOnClickListener {
            val checkedId = binding.rgFilterStates.checkedRadioButtonId
            val status = when (checkedId) {
                R.id.rbPending -> SubmissionStatus.PENDING
                R.id.rbApproved -> SubmissionStatus.APPROVED
                R.id.rbRejected -> SubmissionStatus.REJECTED
                R.id.rbAll -> null
                else -> null
            }

            onFilterSelected?.invoke(status)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}