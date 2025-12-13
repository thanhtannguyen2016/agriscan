package com.agri.agriscan.domain.usecase.plant
import com.agri.agriscan.domain.model.PlantIdentification
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.repository.PlantRepository
import com.agri.agriscan.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 * Use case for identifying plant species from images
 */
class IdentifyPlantUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    /**
     * @param imageUris List of image URIs (max 5)
     * @param organs List of organ types for each image (auto, leaf, flower, fruit, bark)
     * @return Flow of Resource containing PlantIdentification
     */
    suspend operator fun invoke(
        imageUris: List<String>,
        organs: List<String> = List(imageUris.size) { Constants.ORGAN_AUTO }
    ): Flow<Resource<PlantIdentification>> {

        // Validate input
        if (imageUris.isEmpty()) {
            return kotlinx.coroutines.flow.flow {
                emit(Resource.Error("Vui lòng chọn ít nhất một ảnh"))
            }
        }

        if (imageUris.size > Constants.MAX_IMAGES_PER_REQUEST) {
            return kotlinx.coroutines.flow.flow {
                emit(Resource.Error("Chỉ có thể chọn tối đa ${Constants.MAX_IMAGES_PER_REQUEST} ảnh"))
            }
        }

        if (organs.size != imageUris.size) {
            return kotlinx.coroutines.flow.flow {
                emit(Resource.Error("Số lượng organs phải khớp với số lượng ảnh"))
            }
        }

        return plantRepository.identifyPlant(imageUris, organs)
    }
}