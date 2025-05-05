package com.ebcf.jnab.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.data.model.TalkModel
import com.ebcf.jnab.databinding.ItemTalkBinding
import com.ebcf.jnab.ui.viewmodel.TalksListViewModel

class TalksListAdapter(private val talks: List<TalkModel>) :
    RecyclerView.Adapter<TalksListAdapter.TalksListViewHolder>() {

    inner class TalksListViewHolder(private val binding: ItemTalkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(talk: TalkModel) {
            binding.textView.text = talk.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalksListViewHolder {
        val binding = ItemTalkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TalksListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TalksListViewHolder, position: Int) {
        holder.bind(talks[position])

    }

    override fun getItemCount(): Int = talks.size
}
