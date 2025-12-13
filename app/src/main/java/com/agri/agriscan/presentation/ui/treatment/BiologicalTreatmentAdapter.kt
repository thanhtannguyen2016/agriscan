package com.agri.agriscan.presentation.ui.treatment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agri.agriscan.databinding.ItemBiologicalTreatmentBinding
import com.agri.agriscan.domain.model.BiologicalTreatment

class BiologicalTreatmentAdapter :
    ListAdapter<BiologicalTreatment, BiologicalTreatmentAdapter.ViewHolder>(BiologicalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBiologicalTreatmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemBiologicalTreatmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(treatment: BiologicalTreatment) {
            binding.apply {
                tvName.text = treatment.name
                tvDescription.text = treatment.description

                // Materials with bullet points
                if (treatment.materials.isNotEmpty()) {
                    val materialsText = treatment.materials
                        .joinToString("\n") { "• $it" }
                    tvMaterials.text = "Nguyên liệu:\n$materialsText"
                } else {
                    tvMaterials.text = "Nguyên liệu: Không có thông tin"
                }

                // Steps with numbering
                if (treatment.steps.isNotEmpty()) {
                    val stepsText = treatment.steps
                        .mapIndexed { index, step -> "${index + 1}. $step" }
                        .joinToString("\n")
                    tvSteps.text = "Các bước thực hiện:\n$stepsText"
                } else {
                    tvSteps.text = "Các bước: Không có thông tin"
                }

                // Effectiveness
                tvEffectiveness.text = "Hiệu quả: ${treatment.effectiveness}"
            }
        }
    }

    private class BiologicalDiffCallback : DiffUtil.ItemCallback<BiologicalTreatment>() {
        override fun areItemsTheSame(
            oldItem: BiologicalTreatment,
            newItem: BiologicalTreatment
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: BiologicalTreatment,
            newItem: BiologicalTreatment
        ): Boolean {
            return oldItem == newItem
        }
    }
}