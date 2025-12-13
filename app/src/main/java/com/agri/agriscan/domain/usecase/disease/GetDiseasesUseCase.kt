package com.agri.agriscan.domain.usecase.disease
import com.agri.agriscan.domain.model.Disease
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.repository.DiseaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting list of diseases
 */
class GetDiseasesUseCase @Inject constructor(
    private val diseaseRepository: DiseaseRepository
) {
    /**
     * @param searchQuery Optional search prefix to filter diseases
     * @return Flow of Resource containing list of Disease
     */
    suspend operator fun invoke(
        searchQuery: String? = null
    ): Flow<Resource<List<Disease>>> {
        return diseaseRepository.getDiseases()
    }
}