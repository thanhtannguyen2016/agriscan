package com.agri.agriscan.data.remote.dto.openai
import com.google.gson.annotations.SerializedName
// ==================== Treatment Response DTO ====================

/**
 * Parsed treatment response from OpenAI
 * This represents the JSON structure we expect from GPT-4
 */
data class TreatmentResponse(
    @SerializedName("chemicalTreatments")
    val chemicalTreatments: List<ChemicalTreatmentDto>,

    @SerializedName("biologicalTreatments")
    val biologicalTreatments: List<BiologicalTreatmentDto>,

    @SerializedName("prevention")
    val prevention: PreventionDto?,

    @SerializedName("generalAdvice")
    val generalAdvice: String?
)

data class ChemicalTreatmentDto(
    @SerializedName("name")
    val name: String,

    @SerializedName("activeIngredients")
    val activeIngredients: List<String>,

    @SerializedName("dosage")
    val dosage: String,

    @SerializedName("usage")
    val usage: String,

    @SerializedName("precautions")
    val precautions: String?
)

data class BiologicalTreatmentDto(
    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("materials")
    val materials: List<String>,

    @SerializedName("steps")
    val steps: List<String>,

    @SerializedName("effectiveness")
    val effectiveness: String?
)

data class PreventionDto(
    @SerializedName("title")
    val title: String,

    @SerializedName("tips")
    val tips: List<String>
)