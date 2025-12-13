package com.agri.agriscan.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Objects cho PlantNet API Response
 */

/**
 * Response từ PlantNet identify endpoint
 */
data class PlantNetIdentifyResponse(
    @SerializedName("query")
    val query: QueryInfo?,

    @SerializedName("language")
    val language: String?,

    @SerializedName("preferedReferential")
    val preferedReferential: String?,

    @SerializedName("bestMatch")
    val bestMatch: String?,

    @SerializedName("results")
    val results: List<PlantResult>?,

    @SerializedName("version")
    val version: String?,

    @SerializedName("remainingIdentificationRequests")
    val remainingRequests: Int?
)

/**
 * Query information
 */
data class QueryInfo(
    @SerializedName("project")
    val project: String?,

    @SerializedName("images")
    val images: List<String>?,

    @SerializedName("organs")
    val organs: List<String>?,

    @SerializedName("includeRelatedImages")
    val includeRelatedImages: Boolean?
)

/**
 * Plant result item
 */
data class PlantResult(
    @SerializedName("score")
    val score: Float,

    @SerializedName("species")
    val species: SpeciesInfo,

    @SerializedName("gbif")
    val gbif: GbifInfo?,

    @SerializedName("images")
    val images: List<ImageInfo>?
)

/**
 * Species information
 */
data class SpeciesInfo(
    @SerializedName("scientificNameWithoutAuthor")
    val scientificNameWithoutAuthor: String,

    @SerializedName("scientificNameAuthorship")
    val scientificNameAuthorship: String?,

    @SerializedName("scientificName")
    val scientificName: String?,

    @SerializedName("genus")
    val genus: GenusInfo?,

    @SerializedName("family")
    val family: FamilyInfo?,

    @SerializedName("commonNames")
    val commonNames: List<String>?
)

/**
 * Genus information
 */
data class GenusInfo(
    @SerializedName("scientificNameWithoutAuthor")
    val scientificNameWithoutAuthor: String,

    @SerializedName("scientificName")
    val scientificName: String?
)

/**
 * Family information
 */
data class FamilyInfo(
    @SerializedName("scientificNameWithoutAuthor")
    val scientificNameWithoutAuthor: String,

    @SerializedName("scientificName")
    val scientificName: String?
)

/**
 * GBIF (Global Biodiversity Information Facility) info
 */
data class GbifInfo(
    @SerializedName("id")
    val id: String?
)

/**
 * Image information
 */
data class ImageInfo(
    @SerializedName("organ")
    val organ: String?,

    @SerializedName("author")
    val author: String?,

    @SerializedName("license")
    val license: String?,

    @SerializedName("date")
    val date: DateInfo?,

    @SerializedName("citation")
    val citation: String?,

    @SerializedName("url")
    val url: UrlInfo?
)

/**
 * Date information
 */
data class DateInfo(
    @SerializedName("timestamp")
    val timestamp: Long?,

    @SerializedName("string")
    val string: String?
)

/**
 * URL information for different sizes
 */
data class UrlInfo(
    @SerializedName("o")
    val original: String?,

    @SerializedName("m")
    val medium: String?,

    @SerializedName("s")
    val small: String?
)

/**
 * Projects list response
 */
data class PlantNetProjectsResponse(
    @SerializedName("projects")
    val projects: List<String>
)

data class DiseaseInfo(
    @SerializedName("label")
    val label: String,

    @SerializedName("name")
    val name: String, // EPPO code

    @SerializedName("categories")
    val categories: List<String>? = null
)
data class DiseaseIdentificationResponse(
    @SerializedName("query")
    val query: QueryInfo,

    @SerializedName("language")
    val language: String,

    @SerializedName("results")
    val results: List<DiseaseResult>,

    @SerializedName("version")
    val version: String,

    @SerializedName("remainingIdentificationRequests")
    val remainingIdentificationRequests: Int
)


// ==================== Species Identification DTOs ====================

data class SpeciesIdentificationResponse(
    @SerializedName("query")
    val query: QueryInfo,

    @SerializedName("language")
    val language: String,

    @SerializedName("preferedReferential")
    val preferedReferential: String?,

    @SerializedName("bestMatch")
    val bestMatch: String?,

    @SerializedName("results")
    val results: List<SpeciesResult>,

    @SerializedName("version")
    val version: String,

    @SerializedName("remainingIdentificationRequests")
    val remainingIdentificationRequests: Int
)

data class SpeciesResult(
    @SerializedName("score")
    val score: Float,

    @SerializedName("species")
    val species: SpeciesInfo,

    @SerializedName("images")
    val images: List<ImageInfo>? = null,

    @SerializedName("gbif")
    val gbif: GbifInfo? = null
)

// ==================== Variety Identification DTOs ====================

data class VarietyIdentificationResponse(
    @SerializedName("query")
    val query: QueryInfo,

    @SerializedName("language")
    val language: String,

    @SerializedName("results")
    val results: List<VarietySpeciesResult>,

    @SerializedName("version")
    val version: String,

    @SerializedName("remainingIdentificationRequests")
    val remainingIdentificationRequests: Int
)

data class VarietySpeciesResult(
    @SerializedName("score")
    val score: Float,

    @SerializedName("species")
    val species: SpeciesInfo,

    @SerializedName("images")
    val images: List<ImageInfo>? = null,

    @SerializedName("varieties")
    val varieties: List<VarietyResult>,

    @SerializedName("gbif")
    val gbif: GbifInfo? = null,

    @SerializedName("powo")
    val powo: PowoInfo? = null
)

data class VarietyResult(
    @SerializedName("name")
    val name: String,

    @SerializedName("score")
    val score: Float,

    @SerializedName("images")
    val images: List<ImageInfo>? = null
)

data class VarietyInfo(
    @SerializedName("name")
    val name: String,

    @SerializedName("species")
    val species: SpeciesInfo
)

// ==================== Disease Identification DTOs ====================

data class DiseaseResult(
    @SerializedName("name")
    val name: String, // EPPO code

    @SerializedName("score")
    val score: Float,

    @SerializedName("description")
    val description: String?,

    @SerializedName("images")
    val images: List<ImageInfo>? = null
)

// ==================== Common DTOs ====================




data class PowoInfo(
    @SerializedName("id")
    val id: String
)

// ==================== Supporting DTOs ====================

data class ProjectInfo(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("type")
    val type: String?
)

data class LanguageInfo(
    @SerializedName("code")
    val code: String,

    @SerializedName("name")
    val name: String
)