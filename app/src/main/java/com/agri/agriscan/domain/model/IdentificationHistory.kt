package com.agri.agriscan.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class IdentificationHistory(
    val id: Long = 0,
    val imageUri: String,
    val timestamp: Long,
    val plantName: String,
    val plantScientificName: String,
    val diseaseName: String?,
    val diseaseCode: String?,
    val confidence: Float
) : Parcelable

data class Project(
    val id: String,
    val name: String,
    val description: String?
)

data class Language(
    val code: String,
    val name: String
)

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : UiState<Nothing>()
}

// ==================== Resource Wrapper ====================

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}