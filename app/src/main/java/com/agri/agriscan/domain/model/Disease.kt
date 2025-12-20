package com.agri.agriscan.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Disease(
    @PrimaryKey
    val eppoCode: String,
    val name: String,
    val description: String?,
    val confidence: Float,
    val imageUrl: String?,
    val categories: List<String>
) : Parcelable