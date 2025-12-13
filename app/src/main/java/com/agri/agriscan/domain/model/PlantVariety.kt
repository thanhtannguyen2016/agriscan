package com.agri.agriscan.domain.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class PlantVariety(
    val name: String,
    val species: Plant,
    val confidence: Float,
    val imageUrl: String?
) : Parcelable
@Parcelize
data class VarietyIdentification(
    val imageUri: String,
    val timestamp: Long,
    val results: List<VarietyGroup>
) : Parcelable

@Parcelize
data class VarietyGroup(
    val species: Plant,
    val varieties: List<PlantVariety>
) : Parcelable