package com.agri.agriscan.domain.usecase.treatment
import com.agri.agriscan.domain.model.Disease
import com.agri.agriscan.domain.model.Plant
import com.agri.agriscan.domain.model.PlantIdentification
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.model.Treatment
import com.agri.agriscan.domain.repository.PlantRepository
import com.agri.agriscan.domain.repository.TreatmentRepository
import com.agri.agriscan.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
/**
 * Use case for getting treatment recommendations for a plant disease
 */
class GetTreatmentUseCase @Inject constructor(
    private val treatmentRepository: TreatmentRepository
) {
    /**
     * @param plant The affected plant
     * @param disease The diagnosed disease
     * @return Flow of Resource containing Treatment with chemical and biological methods
     */
    suspend operator fun invoke(
        plant: Plant,
        disease: Disease
    ): Flow<Resource<Treatment>> {

        // Validate input
        if (plant.scientificName?.isBlank() == true) {
            return flow {
                emit(Resource.Error("Thông tin cây trồng không hợp lệ"))
            }
        }

        if (disease.name.isBlank()) {
            return flow {
                emit(Resource.Error("Thông tin bệnh không hợp lệ"))
            }
        }

        return treatmentRepository.getTreatment(plant, disease)
    }
}