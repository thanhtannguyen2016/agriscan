package com.agri.agriscan.presentation.ui.treatment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.agri.agriscan.databinding.FragmentPreventionBinding

class PreventionFragment : Fragment() {

    private var _binding: FragmentPreventionBinding? = null
    private val binding get() = _binding!!

    private var preventionMeasures: List<String> = emptyList()
    private var generalAdvice: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreventionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (preventionMeasures.isNotEmpty() || generalAdvice.isNotBlank()) {
            displayPreventionData()
        }
    }

    fun setPreventionData(measures: List<String>, advice: String) {
        this.preventionMeasures = measures
        this.generalAdvice = advice
        if (_binding != null) {
            displayPreventionData()
        }
    }

    private fun displayPreventionData() {
        binding.apply {
            // Prevention measures
            if (preventionMeasures.isNotEmpty()) {
                val measuresText = preventionMeasures
                    .mapIndexed { index, measure -> "${index + 1}. $measure" }
                    .joinToString("\n\n")
                tvPreventionMeasures.text = measuresText
                tvPreventionMeasures.visibility = View.VISIBLE
                tvPreventionTitle.visibility = View.VISIBLE
            } else {
                tvPreventionMeasures.visibility = View.GONE
                tvPreventionTitle.visibility = View.GONE
            }

            // General advice
            if (generalAdvice.isNotBlank()) {
                tvGeneralAdvice.text = generalAdvice
                tvGeneralAdvice.visibility = View.VISIBLE
                tvAdviceTitle.visibility = View.VISIBLE
            } else {
                tvGeneralAdvice.visibility = View.GONE
                tvAdviceTitle.visibility = View.GONE
            }

            // Show empty state if no data
            if (preventionMeasures.isEmpty() && generalAdvice.isBlank()) {
                tvEmptyState.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
            } else {
                tvEmptyState.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}