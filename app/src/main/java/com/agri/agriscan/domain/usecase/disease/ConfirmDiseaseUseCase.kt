package com.agri.agriscan.domain.usecase.disease

import com.agri.agriscan.domain.model.Disease
import com.agri.agriscan.domain.model.DiseaseIdentification
import javax.inject.Inject

/**
 * Use case for confirming a disease diagnosis
 * This updates the DiseaseIdentification with the confirmed disease
 */
class ConfirmDiseaseUseCase @Inject constructor() {
    /**
     * @param identification The current disease identification
     * @param confirmedDisease The disease confirmed by the user
     * @return Updated DiseaseIdentification with confirmed disease
     */
    operator fun invoke(
        identification: DiseaseIdentification,
        confirmedDisease: Disease
    ): DiseaseIdentification {
        return identification.copy(
            confirmedDisease = confirmedDisease
        )
    }
}