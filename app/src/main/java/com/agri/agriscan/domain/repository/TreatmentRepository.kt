package com.agri.agriscan.domain.repository

import com.agri.agriscan.domain.model.Disease
import com.agri.agriscan.domain.model.Plant
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.model.Treatment
import kotlinx.coroutines.flow.Flow

interface TreatmentRepository {

    /**
     * Get treatment recommendations for a specific disease and plant
     * Uses OpenAI GPT-4 to generate comprehensive treatment advice
     * @param plant The affected plant
     * @param disease The identified disease
     * @return Flow of Resource containing Treatment recommendations
     */
    suspend fun getTreatment(
        plant: Plant,
        disease: Disease
    ): Flow<Resource<Treatment>>
}