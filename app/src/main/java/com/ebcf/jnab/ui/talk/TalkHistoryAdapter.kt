package com.ebcf.jnab.ui.talk

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.domain.model.TalkModel
import com.ebcf.jnab.databinding.ItemTalkSpeakerBinding
import com.ebcf.jnab.domain.usecase.FormatDateUseCase

class TalkHistoryAdapter(private val talks: List<TalkModel>, private val formatDateUseCase: FormatDateUseCase) :
    RecyclerView.Adapter<TalkHistoryAdapter.TalkHistoryViewHolder>() {

    inner class TalkHistoryViewHolder(private val binding: ItemTalkSpeakerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(talk: TalkModel, formatDateUseCase: FormatDateUseCase) {
            binding.textView.text = talk.title
            binding.dateTime.text = formatDateUseCase.formatDateTime(talk.date,talk.startTime)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkHistoryViewHolder {
        val binding = ItemTalkSpeakerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TalkHistoryViewHolder(binding)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TalkHistoryViewHolder, position: Int) {
        holder.bind(talks[position], formatDateUseCase)

    }

    override fun getItemCount(): Int = talks.size
}
