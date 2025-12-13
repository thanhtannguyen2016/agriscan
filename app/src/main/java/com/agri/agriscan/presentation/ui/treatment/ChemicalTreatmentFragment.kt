package com.agri.agriscan.presentation.ui.treatment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agri.agriscan.databinding.FragmentChemicalTreatmentBinding
import com.agri.agriscan.domain.model.ChemicalTreatment

class ChemicalTreatmentFragment : Fragment() {

    private var _binding: FragmentChemicalTreatmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChemicalTreatmentAdapter
    private var treatments: List<ChemicalTreatment> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChemicalTreatmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        if (treatments.isNotEmpty()) {
            displayTreatments()
        }
    }

    private fun setupRecyclerView() {
        adapter = ChemicalTreatmentAdapter()
        binding.rvChemicalTreatments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ChemicalTreatmentFragment.adapter
        }
    }

    fun setTreatments(treatments: List<ChemicalTreatment>) {
        this.treatments = treatments
        if (_binding != null) {
            displayTreatments()
        }
    }

    private fun displayTreatments() {
        if (treatments.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvChemicalTreatments.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvChemicalTreatments.visibility = View.VISIBLE
            adapter.submitList(treatments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}