package com.agri.agriscan.domain.repository

import com.agri.agriscan.domain.model.PlantIdentification
import com.agri.agriscan.domain.model.PlantVariety
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.model.VarietyIdentification
import kotlinx.coroutines.flow.Flow

interface PlantRepository {

    /**
     * Identify plant species from images
     * @param imageUris List of image URIs (max 5)
     * @param organs List of organ types matching the images (auto, leaf, flower, fruit, bark)
     * @return Flow of Resource containing PlantIdentification result
     */
    suspend fun identifyPlant(
        imageUris: List<String>,
        organs: List<String> = listOf("auto")
    ): Flow<Resource<PlantIdentification>>

    /**
     * Get list of identifiable plant varieties
     * @param prefix Optional prefix filter for variety names
     * @return Flow of Resource containing list of PlantVariety
     */
    suspend fun getVarieties(
        prefix: String? = null
    ): Flow<Resource<List<PlantVariety>>>

    /**
     * Identify plant variety from images
     * @param imageUris List of image URIs
     * @param organs List of organ types matching the images
     * @return Flow of Resource containing VarietyIdentification result
     */
    suspend fun identifyVariety(
        imageUris: List<String>,
        organs: List<String> = listOf("auto")
    ): Flow<Resource<VarietyIdentification>>
}