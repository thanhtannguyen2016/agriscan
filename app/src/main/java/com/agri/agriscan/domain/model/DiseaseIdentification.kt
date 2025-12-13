package com.agri.agriscan.domain.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class DiseaseIdentification(
    val imageUri: String,
    val plant: Plant,
    val timestamp: Long,
    val results: List<Disease>,
    val confirmedDisease: Disease?
) : Parcelable