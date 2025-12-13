package com.agri.agriscan.presentation.ui.identification.disease

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.agri.agriscan.R
import com.agri.agriscan.databinding.ItemDiseaseResultBinding
import com.agri.agriscan.domain.model.Disease

class DiseaseResultAdapter(
    private val onDiseaseClick: (Disease) -> Unit
) : ListAdapter<Disease, DiseaseResultAdapter.DiseaseViewHolder>(DiseaseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val binding = ItemDiseaseResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DiseaseViewHolder(binding, onDiseaseClick)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiseaseViewHolder(
        private val binding: ItemDiseaseResultBinding,
        private val onDiseaseClick: (Disease) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(disease: Disease) {
            binding.apply {
                // Disease name
                tvDiseaseName.text = disease.name

                // Description
                tvDescription.text = disease.description ?: "Không có mô tả"

                // EPPO Code
                tvEppoCode.text = "Mã: ${disease.eppoCode}"

                // Confidence
                val confidencePercent = (disease.confidence * 100).toInt()
                tvConfidence.text = "$confidencePercent%"
                progressConfidence.progress = confidencePercent

                // Set progress bar color
                val colorRes = when {
                    disease.confidence >= 0.7 -> R.color.success
                    disease.confidence >= 0.4 -> R.color.warning
                    else -> R.color.error
                }
                progressConfidence.progressTintList =
                    itemView.context.getColorStateList(colorRes)

                // Categories
                if (disease.categories.isNotEmpty()) {
                    tvCategories.text = "Loại: ${disease.categories.joinToString(", ")}"
                }

                // Load image
                disease.imageUrl?.let { url ->
                    ivDiseaseImage.load(url) {
                        crossfade(true)
                        placeholder(R.drawable.ic_disease_placeholder)
                        error(R.drawable.ic_disease_placeholder)
                    }
                } ?: run {
                    ivDiseaseImage.setImageResource(R.drawable.ic_disease_placeholder)
                }

                // Click listener
                root.setOnClickListener {
                    onDiseaseClick(disease)
                }
            }
        }
    }

    private class DiseaseDiffCallback : DiffUtil.ItemCallback<Disease>() {
        override fun areItemsTheSame(oldItem: Disease, newItem: Disease): Boolean {
            return oldItem.eppoCode == newItem.eppoCode
        }

        override fun areContentsTheSame(oldItem: Disease, newItem: Disease): Boolean {
            return oldItem == newItem
        }
    }
}