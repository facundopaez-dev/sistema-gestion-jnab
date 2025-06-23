package com.ebcf.jnab.ui.talk.list
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebcf.jnab.R
import com.ebcf.jnab.databinding.ItemGroupedTalksBinding
import com.ebcf.jnab.domain.model.TalkModel
import com.ebcf.jnab.databinding.ItemTalkBinding
import androidx.recyclerview.widget.DiffUtil
import com.ebcf.jnab.databinding.ItemTalkDateHeaderBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


sealed class TalkItem {
    data class DateHeader(val date: LocalDate) : TalkItem()
    data class TalkDetail(val talk: TalkModel) : TalkItem()
}

class GroupedTalksAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<TalkItem>()

    fun submitGroupedTalks(groupedTalks: List<GroupedTalks>) {
        items.clear()
        groupedTalks.forEach { group ->
            items.add(TalkItem.DateHeader(group.date))
            items.addAll(group.talks.map { TalkItem.TalkDetail(it) })
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is TalkItem.DateHeader -> 0
            is TalkItem.TalkDetail -> 1
        }
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_talk_date_header, parent, false)
            DateHeaderViewHolder(view)
        } else {
            val binding = ItemTalkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TalkViewHolder(binding)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is TalkItem.DateHeader -> (holder as DateHeaderViewHolder).bind(item.date)
            is TalkItem.TalkDetail -> (holder as TalkViewHolder).bind(item.talk)
        }
    }

    class DateHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateText: TextView = itemView.findViewById(R.id.dateHeader)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(date: LocalDate) {
            val formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", Locale("es"))
            val formattedDate = date.format(formatter).replaceFirstChar { it.uppercase() }
            dateText.text = formattedDate
        }
    }

    class TalkViewHolder(private val binding: ItemTalkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(talk: TalkModel) {
            binding.textView.text = "${talk.startTime} hs - ${talk.title}"


            binding.btnFavorite.visibility = View.GONE
        }
    }

}

