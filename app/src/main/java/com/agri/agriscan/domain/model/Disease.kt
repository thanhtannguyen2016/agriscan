package com.agri.agriscan.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Disease(
    val eppoCode: String,
    val name: String,
    val description: String?,
    val confidence: Float,
    val imageUrl: String?,
    val categories: List<String>
) : Parcelable