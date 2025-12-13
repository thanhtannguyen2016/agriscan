package com.agri.agriscan.domain.usecase.treatment
import com.agri.agriscan.domain.model.PlantIdentification
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.model.Treatment
import com.agri.agriscan.domain.repository.PlantRepository
import com.agri.agriscan.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 * Use case for filtering biological treatments by name or materials
 */
class FilterBiologicalTreatmentsUseCase @Inject constructor() {
    /**
     * @param treatment The complete treatment data
     * @param query Search query
     * @return Filtered list of biological treatments
     */
    operator fun invoke(
        treatment: Treatment,
        query: String
    ): Treatment {
        if (query.isBlank()) {
            return treatment
        }

        val filteredBiological = treatment.biologicalTreatments.filter { biological ->
            biological.name.contains(query, ignoreCase = true) ||
                    biological.description.contains(query, ignoreCase = true) ||
                    biological.materials.any { it.contains(query, ignoreCase = true) }
        }

        return treatment.copy(biologicalTreatments = filteredBiological)
    }
}