package com.agri.agriscan.domain.repository

import com.agri.agriscan.domain.model.Disease
import com.agri.agriscan.domain.model.DiseaseIdentification
import com.agri.agriscan.domain.model.Plant
import com.agri.agriscan.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface DiseaseRepository {

    /**
     * Identify disease from plant images
     * @param plant The identified plant
     * @param imageUris List of image URIs showing disease symptoms
     * @param organs List of organ types matching the images
     * @return Flow of Resource containing DiseaseIdentification result
     */
    suspend fun identifyDisease(
        plant: Plant,
        imageUris: List<String>,
        organs: List<String> = listOf("auto")
    ): Flow<Resource<DiseaseIdentification>>

    /**
     * Get list of all identifiable diseases
     * @return Flow of Resource containing list of Disease
     */
    suspend fun getDiseases(prefix: String? = null): Flow<Resource<List<Disease>>>
}