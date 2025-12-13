package com.agri.agriscan.domain.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantIdentification(
    val imageUri: String,
    val timestamp: Long,
    val results: List<Plant>,
    val bestMatch: Plant?
) : Parcelable {
    val hasSufficientConfidence: Boolean
        get() = bestMatch?.confidence ?: 0f >= 0.5f
}