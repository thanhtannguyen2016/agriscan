package com.agri.agriscan.data.mapper
import com.agri.agriscan.data.remote.dto.plantnet.*
import com.agri.agriscan.data.remote.dto.openai.TreatmentResponse
import com.agri.agriscan.domain.model.*
import javax.inject.Inject

/**
 * Mapper for Treatment-related data
 */
class TreatmentMapper @Inject constructor() {

    fun mapToTreatment(
        response: TreatmentResponse,
        plant: Plant,
        disease: Disease
    ): Treatment {
        val chemicalTreatments = response.chemicalTreatments.map { dto ->
            ChemicalTreatment(
                name = dto.name,
                activeIngredients = dto.activeIngredients,
                dosage = dto.dosage,
                usage = dto.usage,
                precautions = dto.precautions
            )
        }

        val biologicalTreatments = response.biologicalTreatments.map { dto ->
            BiologicalTreatment(
                name = dto.name,
                description = dto.description,
                materials = dto.materials,
                steps = dto.steps,
                effectiveness = dto.effectiveness
            )
        }

        val prevention = response.prevention?.let { dto ->
            Prevention(
                title = dto.title,
                tips = dto.tips
            )
        }

        return Treatment(
            disease = disease,
            plant = plant,
            chemicalTreatments = chemicalTreatments,
            biologicalTreatments = biologicalTreatments,
            prevention = prevention,
            generalAdvice = response.generalAdvice
        )
    }
}




