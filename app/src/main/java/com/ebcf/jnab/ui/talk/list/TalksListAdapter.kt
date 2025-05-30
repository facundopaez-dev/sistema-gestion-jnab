package com.ebcf.jnab.ui.talk.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.R
import com.ebcf.jnab.domain.model.TalkModel
import com.ebcf.jnab.databinding.ItemTalkBinding


class TalksListAdapter(
    private var talks: List<TalkModel>,
    private var favoriteIds: Set<Int>,
    private val onFavoriteClick: (Int) -> Unit
) : RecyclerView.Adapter<TalksListAdapter.TalksListViewHolder>() {

    inner class TalksListViewHolder(private val binding: ItemTalkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(talk: TalkModel) {
            binding.textView.text = talk.title

            val isFavorite = favoriteIds.contains(talk.id)
            val iconRes = if (isFavorite) {
                R.drawable.star_24dp
            } else {
                R.drawable.star_24_border
            }

            binding.btnFavorite.setImageResource(iconRes)

            binding.btnFavorite.setOnClickListener {
                onFavoriteClick(talk.id)
            }
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

    fun updateTalks(newTalks: List<TalkModel>) {
        this.talks = newTalks
        notifyDataSetChanged()
    }

    fun updateFavorites(newFavorites: Set<Int>) {
        this.favoriteIds = newFavorites
        notifyDataSetChanged()
    }

    fun updateData(newTalks: List<TalkModel>, newFavorites: Set<Int>) {
        talks = newTalks
        favoriteIds = newFavorites
        notifyDataSetChanged() // Para mejor rendimiento, podés usar DiffUtil más adelante
    }
}
