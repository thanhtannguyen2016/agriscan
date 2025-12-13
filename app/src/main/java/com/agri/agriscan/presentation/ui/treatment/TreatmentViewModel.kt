package com.agri.agriscan.presentation.ui.treatment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agri.agriscan.domain.model.*
import com.agri.agriscan.domain.usecase.treatment.GetTreatmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreatmentViewModel @Inject constructor(
    private val getTreatmentUseCase: GetTreatmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Treatment>>(UiState.Idle)
    val uiState: StateFlow<UiState<Treatment>> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(TreatmentTab.CHEMICAL)
    val selectedTab: StateFlow<TreatmentTab> = _selectedTab.asStateFlow()

    private val _plant = MutableStateFlow<Plant?>(null)
    val plant: StateFlow<Plant?> = _plant.asStateFlow()

    private val _disease = MutableStateFlow<Disease?>(null)
    val disease: StateFlow<Disease?> = _disease.asStateFlow()

    /**
     * Get treatment recommendations
     */
    fun getTreatment(plant: Plant, disease: Disease) {
        _plant.value = plant
        _disease.value = disease

        viewModelScope.launch {
            getTreatmentUseCase(
                plant = plant,
                disease = disease
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = UiState.Loading
                    }
                    is Resource.Success -> {
                        _uiState.value = UiState.Success(resource.data)
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.Error(resource.message)
                    }
                }
            }
        }
    }

    /**
     * Select treatment tab
     */
    fun selectTab(tab: TreatmentTab) {
        _selectedTab.value = tab
    }

    /**
     * Get current treatment data
     */
    fun getCurrentTreatment(): Treatment? {
        return (_uiState.value as? UiState.Success)?.data
    }

    /**
     * Retry getting treatment
     */
    fun retry() {
        val p = _plant.value
        val d = _disease.value
        if (p != null && d != null) {
            getTreatment(p, d)
        }
    }

    /**
     * Reset state
     */
    fun reset() {
        _uiState.value = UiState.Idle
        _selectedTab.value = TreatmentTab.CHEMICAL
        _plant.value = null
        _disease.value = null
    }
}

/**
 * Treatment tabs
 */
enum class TreatmentTab {
    CHEMICAL,   // Chemical treatments
    BIOLOGICAL, // Biological treatments
    PREVENTION  // Prevention tips
}