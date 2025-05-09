package com.ebcf.jnab.ui.adapter.organizer

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.data.model.SymposiumModel
import com.ebcf.jnab.databinding.ItemSymposiumOrganizerBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase

class OrganizerSymposiumAdapter(
    private val symposiums: List<SymposiumModel>,
    private val formatDateUseCase: FormatDateUseCase
) : RecyclerView.Adapter<OrganizerSymposiumAdapter.SymposiumViewHolder>() {

    class SymposiumViewHolder(private val binding: ItemSymposiumOrganizerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(symposium: SymposiumModel, formatDateUseCase: FormatDateUseCase) {
            binding.tittle.text = symposium.title
            binding.startTime.text = formatDateUseCase.execute(symposium.startDateTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymposiumViewHolder {
        val binding = ItemSymposiumOrganizerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return SymposiumViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SymposiumViewHolder, position: Int) {
        holder.bind(symposiums[position], formatDateUseCase)
    }

    override fun getItemCount(): Int = symposiums.size
}