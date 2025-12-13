package com.agri.agriscan.domain.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plant(
    val scientificName: String?,
    val commonNames: List<String>,
    val genus: String?,
    val family: String?,
    val confidence: Float,
    val imageUrl: String?,
    val gbifId: String?
) : Parcelable