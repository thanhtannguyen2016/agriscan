package com.agri.agriscan.presentation.ui.identification.plant


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agri.agriscan.domain.model.Plant
import com.agri.agriscan.domain.model.PlantIdentification
import com.agri.agriscan.domain.model.Resource
import com.agri.agriscan.domain.model.UiState
import com.agri.agriscan.domain.usecase.plant.GetPlantVarietiesUseCase
import com.agri.agriscan.domain.usecase.plant.IdentifyPlantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantIdentificationViewModel @Inject constructor(
    private val identifyPlantUseCase: IdentifyPlantUseCase,
    private val getPlantVarietiesUseCase: GetPlantVarietiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<PlantIdentification>>(UiState.Idle)
    val uiState: StateFlow<UiState<PlantIdentification>> = _uiState.asStateFlow()

    private val _selectedPlant = MutableStateFlow<Plant?>(null)
    val selectedPlant: StateFlow<Plant?> = _selectedPlant.asStateFlow()

    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri: StateFlow<String?> = _imageUri.asStateFlow()

    /**
     * Identify plant from image
     */
    fun identifyPlant(imageUri: String, organs: List<String> = listOf("auto")) {
        _imageUri.value = imageUri

        viewModelScope.launch {
            identifyPlantUseCase(
                imageUris = listOf(imageUri),
                organs = organs
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = UiState.Loading
                    }
                    is Resource.Success -> {
                        _uiState.value = UiState.Success(resource.data)

                        // Auto-select best match if confidence is sufficient
                        if (resource.data.hasSufficientConfidence) {
                            _selectedPlant.value = resource.data.bestMatch
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.Error(resource.message)
                    }
                }
            }
        }
    }

    /**
     * Select a plant from the results
     */
    fun selectPlant(plant: Plant) {
        _selectedPlant.value = plant
    }

    /**
     * Check if selected plant has sufficient confidence to proceed
     */
    fun canProceedToDisease(): Boolean {
        return _selectedPlant.value != null
    }

    /**
     * Reset state
     */
    fun reset() {
        _uiState.value = UiState.Idle
        _selectedPlant.value = null
        _imageUri.value = null
    }

    /**
     * Get the current identification result
     */
    fun getCurrentIdentification(): PlantIdentification? {
        return (_uiState.value as? UiState.Success)?.data
    }
}