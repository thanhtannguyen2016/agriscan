package com.agri.agriscan.domain.usecase.disease
import com.agri.agriscan.domain.model.DiseaseIdentification
import com.agri.agriscan.domain.model.Plant
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.repository.DiseaseRepository
import com.agri.agriscan.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
/**
 * Use case for identifying plant diseases from images
 */
class IdentifyDiseaseUseCase @Inject constructor(
    private val diseaseRepository: DiseaseRepository
) {
    /**
     * @param imageUris List of image URIs showing the diseased plant
     * @param organs List of organ types for each image
     * @param plant The plant that is being diagnosed (optional)
     * @return Flow of Resource containing DiseaseIdentification
     */
    suspend operator fun invoke(
        imageUris: List<String>,
        organs: List<String> = List(imageUris.size) { Constants.ORGAN_AUTO },
        plant: Plant
    ): Flow<Resource<DiseaseIdentification>> {

        // Validate input
        if (imageUris.isEmpty()) {
            return flow {
                emit(Resource.Error("Vui lòng chọn ít nhất một ảnh"))
            }
        }

        if (imageUris.size > Constants.MAX_IMAGES_PER_REQUEST) {
            return flow {
                emit(Resource.Error("Chỉ có thể chọn tối đa ${Constants.MAX_IMAGES_PER_REQUEST} ảnh"))
            }
        }

        if (organs.size != imageUris.size) {
            return flow {
                emit(Resource.Error("Số lượng organs phải khớp với số lượng ảnh"))
            }
        }

        return diseaseRepository.identifyDisease(plant, imageUris)
    }
}