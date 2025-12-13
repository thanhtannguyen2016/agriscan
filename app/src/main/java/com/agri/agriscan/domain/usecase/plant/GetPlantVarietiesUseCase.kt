package com.agri.agriscan.domain.usecase.plant
import com.agri.agriscan.domain.model.PlantVariety
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
/**
 * Use case for getting list of plant varieties
 */
class GetPlantVarietiesUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    /**
     * @param searchQuery Optional search prefix to filter varieties
     * @return Flow of Resource containing list of PlantVariety
     */
    suspend operator fun invoke(
        searchQuery: String? = null
    ): Flow<Resource<List<PlantVariety>>> {
        return plantRepository.getVarieties(searchQuery)
    }
}