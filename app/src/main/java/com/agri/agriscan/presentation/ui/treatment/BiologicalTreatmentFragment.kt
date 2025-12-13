package com.agri.agriscan.presentation.ui.treatment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agri.agriscan.databinding.FragmentBiologicalTreatmentBinding
import com.agri.agriscan.domain.model.BiologicalTreatment

class BiologicalTreatmentFragment : Fragment() {

    private var _binding: FragmentBiologicalTreatmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BiologicalTreatmentAdapter
    private var treatments: List<BiologicalTreatment> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBiologicalTreatmentBinding.inflate(inflater, container, false)
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
        adapter = BiologicalTreatmentAdapter()
        binding.rvBiologicalTreatments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@BiologicalTreatmentFragment.adapter
        }
    }

    fun setTreatments(treatments: List<BiologicalTreatment>) {
        this.treatments = treatments
        if (_binding != null) {
            displayTreatments()
        }
    }

    private fun displayTreatments() {
        if (treatments.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvBiologicalTreatments.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvBiologicalTreatments.visibility = View.VISIBLE
            adapter.submitList(treatments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}