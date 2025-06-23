package com.ebcf.jnab.ui.submission

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.databinding.ItemSubmissionBinding
import com.ebcf.jnab.domain.model.Submission
import com.ebcf.jnab.domain.model.SubmissionStatus
import com.ebcf.jnab.domain.usecase.FormatDateUseCase
import java.time.format.DateTimeFormatter

class SubmissionsListAdapter(
    private val formatDateUseCase: FormatDateUseCase,
    private val onItemClick: (Submission) -> Unit
) : ListAdapter<Submission, SubmissionsListAdapter.SubmissionViewHolder>(SubmissionDiffCallback()) {

    private var allSubmissions: List<Submission> = emptyList()

    inner class SubmissionViewHolder(private val binding: ItemSubmissionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(submission: Submission) {
            binding.title.text = submission.title
            binding.author.text = "Autor: ${submission.author}"
            binding.status.text = "Estado: ${submission.status.displayName}"

            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'a las' HH:mm")
            binding.submittedAt.text = "Enviado el ${submission.submittedAt.format(formatter)}"

            if (submission.status == SubmissionStatus.REJECTED) {
                binding.rejectionReason.visibility = View.VISIBLE
                binding.rejectionReason.text = "Motivo: ${submission.rejectionReason}"
            } else {
                binding.rejectionReason.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onItemClick(submission)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionViewHolder {
        val binding = ItemSubmissionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SubmissionViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        val submission = getItem(position)
        holder.bind(submission)
    }

    // Guarda toda la lista para poder filtrar después
    fun setAllSubmissions(submissions: List<Submission>) {
        allSubmissions = submissions
        submitList(submissions)
    }

    fun filterByStatus(status: SubmissionStatus?) {
        val filtered = when (status) {
            null -> allSubmissions
            else -> allSubmissions.filter { it.status == status }
        }
        submitList(filtered)
    }

    class SubmissionDiffCallback : DiffUtil.ItemCallback<Submission>() {
        override fun areItemsTheSame(oldItem: Submission, newItem: Submission): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Submission, newItem: Submission): Boolean {
            return oldItem == newItem
        }
    }
}