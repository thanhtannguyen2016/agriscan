package com.agri.agriscan.data.mapper
import com.agri.agriscan.data.remote.dto.DiseaseIdentificationResponse
import com.agri.agriscan.data.remote.dto.DiseaseInfo
import com.agri.agriscan.domain.model.*
import javax.inject.Inject
import kotlin.collections.map

/**
 * Mapper for Disease-related data
 */
class DiseaseMapper @Inject constructor() {

    fun mapToDisease(diseaseInfo: DiseaseInfo): Disease {
        return Disease(
            eppoCode = diseaseInfo.name,
            name = diseaseInfo.label,
            description = null,
            confidence = 1.0f, // Not provided in list endpoint
            imageUrl = null,
            categories = diseaseInfo.categories ?: emptyList()
        )
    }

    fun mapToDiseaseIdentification(
        response: DiseaseIdentificationResponse,
        imageUri: String,
        plant: Plant? = null
    ): DiseaseIdentification {
        val diseases = response.results.map { result ->
            Disease(
                eppoCode = result.name,
                name = result.description ?: result.name,
                description = result.description,
                confidence = result.score,
                imageUrl = result.images?.firstOrNull()?.url?.medium,
                categories = emptyList() // Not provided in identification response
            )
        }

        return DiseaseIdentification(
            imageUri = imageUri,
            plant = plant ?: createPlaceholderPlant(),
            timestamp = System.currentTimeMillis(),
            results = diseases,
            confirmedDisease = null
        )
    }

    private fun createPlaceholderPlant(): Plant {
        return Plant(
            scientificName = "Unknown",
            commonNames = listOf("Cây chưa xác định"),
            genus = null,
            family = null,
            confidence = 0f,
            imageUrl = null,
            gbifId = ""
        )
    }
}