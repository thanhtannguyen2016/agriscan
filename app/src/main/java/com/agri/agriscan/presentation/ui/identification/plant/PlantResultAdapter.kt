package com.agri.agriscan.presentation.ui.identification.plant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.agri.agriscan.R
import com.agri.agriscan.databinding.ItemPlantResultBinding
import com.agri.agriscan.domain.model.Plant

class PlantResultAdapter(
    private val onPlantClick: (Plant) -> Unit
) : ListAdapter<Plant, PlantResultAdapter.PlantViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlantViewHolder(binding, onPlantClick)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlantViewHolder(
        private val binding: ItemPlantResultBinding,
        private val onPlantClick: (Plant) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(plant: Plant) {
            binding.apply {
                // Scientific name
                tvScientificName.text = plant.scientificName

                // Common names
                val commonNames = if (plant.commonNames.isNotEmpty()) {
                    plant.commonNames.joinToString(", ")
                } else {
                    "Không có tên thông thường"
                }
                tvCommonNames.text = commonNames

                // Family info
                val familyText = plant.family?.let { "Họ: $it" } ?: ""
                tvFamily.text = familyText

                // Confidence
                val confidencePercent = (plant.confidence * 100).toInt()
                tvConfidence.text = "$confidencePercent%"
                progressConfidence.progress = confidencePercent

                // Set progress bar color based on confidence
                val colorRes = when {
                    plant.confidence >= 0.7 -> R.color.success
                    plant.confidence >= 0.4 -> R.color.warning
                    else -> R.color.error
                }
                progressConfidence.progressTintList =
                    itemView.context.getColorStateList(colorRes)

                // Load image
                plant.imageUrl?.let { url ->
                    ivPlantImage.load(url) {
                        crossfade(true)
                        placeholder(R.drawable.ic_plant_placeholder)
                        error(R.drawable.ic_plant_placeholder)
                    }
                } ?: run {
                    ivPlantImage.setImageResource(R.drawable.ic_plant_placeholder)
                }

                // Click listener
                root.setOnClickListener {
                    onPlantClick(plant)
                }
            }
        }
    }

    private class PlantDiffCallback : DiffUtil.ItemCallback<Plant>() {
        override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem.scientificName == newItem.scientificName
        }

        override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem == newItem
        }
    }
}