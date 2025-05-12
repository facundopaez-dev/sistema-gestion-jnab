package com.ebcf.jnab.ui.adapter.organizer

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.domain.model.Submission
import com.ebcf.jnab.databinding.ItemSubmissionBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase
import java.time.format.DateTimeFormatter

class SubmissionAdapter(
    private val submissions: List<Submission>,
    private val formatDateUseCase: FormatDateUseCase
) : RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder>() {

    class SubmissionViewHolder(private val binding: ItemSubmissionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(submission: Submission, formatDateUseCase: FormatDateUseCase) {
            binding.title.text = submission.title
            binding.author.text = submission.author
            binding.status.text = "Estado: ${submission.status.displayName}"

            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'a las' HH:mm 'h'")
            binding.submittedAt.text = "Enviado el ${submission.submittedAt.format(formatter)}"
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
        holder.bind(submissions[position], formatDateUseCase)
    }

    override fun getItemCount(): Int = submissions.size
}