package com.agri.agriscan.domain.usecase.treatment
import com.agri.agriscan.domain.model.PlantIdentification
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.model.Treatment
import com.agri.agriscan.domain.repository.PlantRepository
import com.agri.agriscan.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 * Use case for filtering chemical treatments by active ingredient
 */
class FilterChemicalTreatmentsUseCase @Inject constructor() {
    /**
     * @param treatment The complete treatment data
     * @param ingredientQuery Search query for active ingredients
     * @return Filtered list of chemical treatments
     */
    operator fun invoke(
        treatment: Treatment,
        ingredientQuery: String
    ): Treatment {
        if (ingredientQuery.isBlank()) {
            return treatment
        }

        val filteredChemicals = treatment.chemicalTreatments.filter { chemical ->
            chemical.activeIngredients.any { ingredient ->
                ingredient.contains(ingredientQuery, ignoreCase = true)
            } || chemical.name.contains(ingredientQuery, ignoreCase = true)
        }

        return treatment.copy(chemicalTreatments = filteredChemicals)
    }
}