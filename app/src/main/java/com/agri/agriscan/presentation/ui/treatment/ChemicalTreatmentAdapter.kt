package com.agri.agriscan.presentation.ui.treatment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agri.agriscan.databinding.ItemChemicalTreatmentBinding
import com.agri.agriscan.domain.model.ChemicalTreatment

class ChemicalTreatmentAdapter :
    ListAdapter<ChemicalTreatment, ChemicalTreatmentAdapter.ViewHolder>(ChemicalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChemicalTreatmentBinding.inflate(
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
        private val binding: ItemChemicalTreatmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(treatment: ChemicalTreatment) {
            binding.apply {
                tvName.text = treatment.name

                // Active ingredients with bullet points
                val ingredientsText = treatment.activeIngredients
                    .joinToString("\n") { "• $it" }
                tvActiveIngredients.text = ingredientsText

                tvDosage.text = treatment.dosage
                tvUsage.text = treatment.usage

                // Show precautions if available
                if (!treatment.precautions.isNullOrBlank()) {
                    cardPrecautions.visibility = android.view.View.VISIBLE
                    tvPrecautions.text = treatment.precautions
                } else {
                    cardPrecautions.visibility = android.view.View.GONE
                }
            }
        }
    }

    private class ChemicalDiffCallback : DiffUtil.ItemCallback<ChemicalTreatment>() {
        override fun areItemsTheSame(
            oldItem: ChemicalTreatment,
            newItem: ChemicalTreatment
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: ChemicalTreatment,
            newItem: ChemicalTreatment
        ): Boolean {
            return oldItem == newItem
        }
    }
}