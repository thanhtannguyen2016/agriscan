package com.agri.agriscan.domain.usecase.plant
import com.agri.agriscan.domain.model.PlantIdentification
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.repository.PlantRepository
import com.agri.agriscan.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for identifying plant variety from images
 */
class IdentifyPlantVarietyUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    /**
     * @param imageUris List of image URIs (max 5)
     * @param organs List of organ types for each image
     * @return Flow of Resource containing VarietyIdentification
     */
    suspend operator fun invoke(
        imageUris: List<String>,
        organs: List<String> = List(imageUris.size) { Constants.ORGAN_AUTO }
    ): Flow<Resource<com.agri.agriscan.domain.model.VarietyIdentification>> {

        if (imageUris.isEmpty()) {
            return kotlinx.coroutines.flow.flow {
                emit(Resource.Error("Vui lòng chọn ít nhất một ảnh"))
            }
        }

        return plantRepository.identifyVariety(imageUris, organs)
    }
}