package com.agri.agriscan.domain.model

data class BiologicalTreatment(
    val name: String,
    val description: String,
    val materials: List<String>,
    val steps: List<String>,
    val effectiveness: String?
)

data class Prevention(
    val title: String,
    val tips: List<String>
)