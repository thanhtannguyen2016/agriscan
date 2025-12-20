package com.agri.agriscan.domain.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @Embedded(prefix = "plant_")
    val plant: Plant,
    @Embedded(prefix = "disease_")
    val disease: Disease,
    val imageUri: String,
    val date: String
) : Parcelable