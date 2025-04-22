package com.ebcf.jnab.ui.symposiums

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.data.model.Symposium
import com.ebcf.jnab.databinding.ItemSymposiumBinding
import java.time.format.DateTimeFormatter
import java.util.Locale

class SymposiumsAdapter(private val symposiums: List<Symposium>) :
    RecyclerView.Adapter<SymposiumsAdapter.SymposiumViewHolder>() {

    class SymposiumViewHolder(private val binding: ItemSymposiumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(symposium: Symposium) {
            binding.symposiumTitle.text = symposium.title


            val formatter = DateTimeFormatter.ofPattern(
                "d 'de' MMMM 'de' yyyy, HH:mm 'hs'",
                Locale("es", "ES")
            )
            binding.dateTime.text = symposium.dateTime.format(formatter)

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
        holder.bind(symposiums[position])
    }

    override fun getItemCount(): Int = symposiums.size
}