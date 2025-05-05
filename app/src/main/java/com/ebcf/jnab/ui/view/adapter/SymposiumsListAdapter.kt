package com.ebcf.jnab.ui.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.data.model.SymposiumModel
import com.ebcf.jnab.databinding.ItemSymposiumBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase

class SymposiumsListAdapter(
    private val symposiums: List<SymposiumModel>,
    private val formatDateUseCase: FormatDateUseCase,
    private val onItemClick: (SymposiumModel) -> Unit

) : RecyclerView.Adapter<SymposiumsListAdapter.SymposiumViewHolder>() {

    class SymposiumViewHolder(private val binding: ItemSymposiumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(symposium: SymposiumModel, formatDateUseCase: FormatDateUseCase) {
            binding.symposiumTitle.text = symposium.title
            binding.dateTime.text = formatDateUseCase.execute(symposium.dateTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymposiumViewHolder {
        val binding = ItemSymposiumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SymposiumViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SymposiumViewHolder, position: Int) {
        val symposium = symposiums[position]
        holder.bind(symposium, formatDateUseCase)
        holder.itemView.setOnClickListener {
            onItemClick(symposium)
        }
    }

    override fun getItemCount(): Int = symposiums.size
}