package com.agri.agriscan.domain.model

data class ChemicalTreatment(
    val name: String,
    val activeIngredients: List<String>,
    val dosage: String,
    val usage: String,
    val precautions: String?
)