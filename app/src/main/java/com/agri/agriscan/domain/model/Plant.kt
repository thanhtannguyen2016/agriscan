package com.agri.agriscan.domain.model
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity

data class Plant(
    val scientificName: String?,
    val commonNames: List<String>,
    val genus: String?,
    val family: String?,
    val confidence: Float,
    val imageUrl: String?,
    @PrimaryKey
    @NonNull
    val gbifId: String
) : Parcelable