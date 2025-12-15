package com.agri.agriscan.presentation.ui.identification.disease

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agri.agriscan.domain.model.*
import com.agri.agriscan.domain.usecase.disease.ConfirmDiseaseUseCase
import com.agri.agriscan.domain.usecase.disease.IdentifyDiseaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiseaseIdentificationViewModel @Inject constructor(
    private val identifyDiseaseUseCase: IdentifyDiseaseUseCase,
    private val confirmDiseaseUseCase: ConfirmDiseaseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<DiseaseIdentification>>(UiState.Idle)
    val uiState: StateFlow<UiState<DiseaseIdentification>> = _uiState.asStateFlow()

    private val _plant = MutableStateFlow<Plant?>(null)
    val plant: StateFlow<Plant?> = _plant.asStateFlow()

    private val _confirmedDisease = MutableStateFlow<Disease?>(null)
    val confirmedDisease: StateFlow<Disease?> = _confirmedDisease.asStateFlow()

    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri: StateFlow<String?> = _imageUri.asStateFlow()

    /**
     * Set the plant for disease identification
     */
    fun setPlant(plant: Plant) {
        _plant.value = plant
    }

    /**
     * Identify disease from image
     */
    fun identifyDisease(imageUri: String, organs: List<String> = listOf("auto")) {
        val currentPlant = _plant.value
        if (currentPlant == null) {
            _uiState.value = UiState.Error("Chưa có thông tin cây trồng")
            return
        }
        _imageUri.value = imageUri

        viewModelScope.launch {
            identifyDiseaseUseCase(
                imageUris = listOf(imageUri),
                organs = organs,
                plant = currentPlant
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = UiState.Loading
                    }
                    is Resource.Success -> {
                        // Update plant in identification if available
                        val updatedIdentification = if (_plant.value != null) {
                            resource.data.copy(plant = _plant.value!!)
                        } else {
                            resource.data
                        }
                        _uiState.value = UiState.Success(updatedIdentification)
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.Error(resource.message)
                    }
                }
            }
        }
    }

    /**
     * Confirm a disease from the results
     */
    fun confirmDisease(disease: Disease) {
        _confirmedDisease.value = disease

        // Update identification with confirmed disease
        val currentIdentification = (_uiState.value as? UiState.Success)?.data
        if (currentIdentification != null) {
            val updatedIdentification = confirmDiseaseUseCase(
                identification = currentIdentification,
                confirmedDisease = disease
            )
            _uiState.value = UiState.Success(updatedIdentification)
        }
    }

    /**
     * Check if user has confirmed a disease
     */
    fun isDiseaseConfirmed(): Boolean {
        return _confirmedDisease.value != null
    }

    /**
     * Get current disease identification
     */
    fun getCurrentIdentification(): DiseaseIdentification? {
        return (_uiState.value as? UiState.Success)?.data
    }

    /**
     * Can proceed to treatment screen
     */
    fun canProceedToTreatment(): Boolean {
        return _confirmedDisease.value != null && _plant.value != null
    }

    /**
     * Reset state
     */
    fun reset() {
        _uiState.value = UiState.Idle
        _confirmedDisease.value = null
        _imageUri.value = null
    }
}