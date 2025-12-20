package com.agri.agriscan.domain.usecase.disease

import com.agri.agriscan.domain.model.Disease
import com.agri.agriscan.domain.model.DiseaseIdentification
import com.agri.agriscan.domain.model.History
import com.agri.agriscan.domain.repository.HistoryRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Use case for confirming a disease diagnosis
 * This updates the DiseaseIdentification with the confirmed disease and saves it to history
 */
class ConfirmDiseaseUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    /**
     * @param identification The current disease identification
     * @param confirmedDisease The disease confirmed by the user
     * @return Updated DiseaseIdentification with confirmed disease
     */
    suspend operator fun invoke(
        identification: DiseaseIdentification,
        confirmedDisease: Disease
    ): DiseaseIdentification {
        // Save to history
        val history = History(
            plant = identification.plant,
            disease = confirmedDisease,
            imageUri = identification.imageUri,
            date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        )
        historyRepository.insertHistory(history)

        // Return updated identification
        return identification.copy(
            confirmedDisease = confirmedDisease
        )
    }
}