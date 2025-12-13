package com.agri.agriscan.data.mapper
import com.agri.agriscan.data.remote.dto.SpeciesIdentificationResponse
import com.agri.agriscan.data.remote.dto.VarietyIdentificationResponse
import com.agri.agriscan.data.remote.dto.VarietyInfo
import com.agri.agriscan.domain.model.*
import javax.inject.Inject

class PlantMapper @Inject constructor() {

    fun mapToPlantIdentification(
        response: SpeciesIdentificationResponse,
        imageUri: String
    ): PlantIdentification {
        val plants = response.results.map { result ->
            Plant(
                scientificName = result.species.scientificName,
                commonNames = result.species.commonNames ?: emptyList(),
                genus = result.species.genus?.scientificNameWithoutAuthor,
                family = result.species.family?.scientificNameWithoutAuthor,
                confidence = result.score,
                imageUrl = result.images?.firstOrNull()?.url?.medium,
                gbifId = result.gbif?.id
            )
        }

        return PlantIdentification(
            imageUri = imageUri,
            timestamp = System.currentTimeMillis(),
            results = plants,
            bestMatch = plants.firstOrNull()
        )
    }

    fun mapToPlantVariety(varietyInfo: VarietyInfo): PlantVariety {
        return PlantVariety(
            name = varietyInfo.name,
            species = Plant(
                scientificName = varietyInfo.species.scientificName,
                commonNames = varietyInfo.species.commonNames ?: emptyList(),
                genus = varietyInfo.species.genus?.scientificNameWithoutAuthor,
                family = varietyInfo.species.family?.scientificNameWithoutAuthor,
                confidence = 1.0f, // Not provided in list endpoint
                imageUrl = null,
                gbifId = null
            ),
            confidence = 1.0f,
            imageUrl = null
        )
    }

    fun mapToVarietyIdentification(
        response: VarietyIdentificationResponse,
        imageUri: String
    ): VarietyIdentification {
        val varietyGroups = response.results.map { speciesResult ->
            val species = Plant(
                scientificName = speciesResult.species.scientificName,
                commonNames = speciesResult.species.commonNames ?: emptyList(),
                genus = speciesResult.species.genus?.scientificNameWithoutAuthor,
                family = speciesResult.species.family?.scientificNameWithoutAuthor,
                confidence = speciesResult.score,
                imageUrl = speciesResult.images?.firstOrNull()?.url?.medium,
                gbifId = speciesResult.gbif?.id
            )

            val varieties = speciesResult.varieties.map { varietyResult ->
                PlantVariety(
                    name = varietyResult.name,
                    species = species,
                    confidence = varietyResult.score,
                    imageUrl = varietyResult.images?.firstOrNull()?.url?.medium
                )
            }

            VarietyGroup(
                species = species,
                varieties = varieties
            )
        }

        return VarietyIdentification(
            imageUri = imageUri,
            timestamp = System.currentTimeMillis(),
            results = varietyGroups
        )
    }
}