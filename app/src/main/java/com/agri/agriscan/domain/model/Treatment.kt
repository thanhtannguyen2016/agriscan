package com.agri.agriscan.domain.model

data class Treatment(
    val disease: Disease,
    val plant: Plant,
    val chemicalTreatments: List<ChemicalTreatment>,
    val biologicalTreatments: List<BiologicalTreatment>,
    val prevention: Prevention?,
    val generalAdvice: String?
)