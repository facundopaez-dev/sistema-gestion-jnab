package com.ebcf.jnab.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.domain.model.SpeakerModel
import com.ebcf.jnab.databinding.ItemSpeakerBinding

class SpeakersListAdapter(private val speakers: List<SpeakerModel>, private val onItemClick: (SpeakerModel) -> Unit
) :
    RecyclerView.Adapter<SpeakersListAdapter.SpeakerViewHolder>() {

    inner class SpeakerViewHolder(private val binding: ItemSpeakerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(speaker: SpeakerModel) {
            binding.speakerName.text = "${speaker.firstName} ${speaker.lastName}"
            binding.speakerInstitution.text = speaker.institution
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder {
        val binding = ItemSpeakerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpeakerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) {
        val speaker = speakers[position]
        holder.bind(speaker)
        holder.itemView.setOnClickListener {
            onItemClick(speaker)
        }
    }
    override fun getItemCount(): Int = speakers.size
}
