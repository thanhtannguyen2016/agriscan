package com.agri.agriscan.presentation.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.agri.agriscan.R
import com.agri.agriscan.databinding.ItemHistoryBinding
import com.agri.agriscan.domain.model.History

class HistoryAdapter(private val onItemClick: (History) -> Unit) :
    ListAdapter<History, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = getItem(position)
        holder.bind(historyItem)
        holder.itemView.setOnClickListener {
            onItemClick(historyItem)
        }
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.tvDiseaseName.text = history.disease.name
            binding.tvPlantName.text = history.plant.commonNames.firstOrNull() ?: history.plant.scientificName
            binding.tvDate.text = history.date
            binding.ivPlantImage.load(history.imageUri) {
                crossfade(true)
                placeholder(R.drawable.ic_leaf)
            }
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem == newItem
        }
    }
}