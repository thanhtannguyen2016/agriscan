package com.agri.agriscan.presentation.ui.treatment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.agri.agriscan.domain.model.Treatment

class TreatmentPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private var treatment: Treatment? = null
    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.add(ChemicalTreatmentFragment())
        fragments.add(BiologicalTreatmentFragment())
        fragments.add(PreventionFragment())
    }

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun setTreatment(treatment: Treatment) {
        this.treatment = treatment
        // Update fragments with treatment data
        fragments.forEach { fragment ->
            when (fragment) {
                is ChemicalTreatmentFragment -> {
                    fragment.setTreatments(treatment.chemicalTreatments)
                }
                is BiologicalTreatmentFragment -> {
                    fragment.setTreatments(treatment.biologicalTreatments)
                }
                is PreventionFragment -> {
                    val preventionTips = treatment.prevention?.tips ?: emptyList()
                    fragment.setPreventionData(
                        preventionTips,              // ✅ Type: List<String>
                        treatment.generalAdvice      // Type: String?
                    )
                }
            }
        }
    }
}