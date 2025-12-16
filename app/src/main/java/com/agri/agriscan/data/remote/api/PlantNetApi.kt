package com.agri.agriscan.data.remote.api

import com.agri.agriscan.data.remote.dto.DiseaseIdentificationResponse
import com.agri.agriscan.data.remote.dto.DiseaseInfo
import com.agri.agriscan.data.remote.dto.LanguageInfo
import com.agri.agriscan.data.remote.dto.ProjectInfo
import com.agri.agriscan.data.remote.dto.SpeciesIdentificationResponse
import com.agri.agriscan.data.remote.dto.VarietyIdentificationResponse
import com.agri.agriscan.data.remote.dto.VarietyInfo
import com.agri.agriscan.data.remote.dto.plantnet.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * PlantNet API Interface
 * Base URL: https://my-api.plantnet.org/v2/
 */
interface PlantNetApi {

    // ==================== Species Identification ====================

    /**
     * Identify plant species from images
     * POST /v2/identify/{project}
     *
     * @param project Project ID (e.g., "all", "k-world-flora")
     * @param apiKey PlantNet API key
     * @param images List of image files (max 5)
     * @param organs List of organ types corresponding to each image (auto, leaf, flower, fruit, bark)
     * @param lang Language code (en, vi, fr, etc.)
     * @param includeRelatedImages Include similar images in results
     * @param noReject Don't reject non-plant images
     * @param nbResults Limit number of results
     */
    @Multipart
    @POST("identify/{project}")
    suspend fun identifySpecies(
        @Path("project") project: String,
        @Part images: List<MultipartBody.Part>,
        @Part("organs") organs: List<@JvmSuppressWildcards RequestBody>,
        @Query("lang") lang: String? = null,
        @Query("include-related-images") includeRelatedImages: Boolean? = null,
        @Query("no-reject") noReject: Boolean? = null,
        @Query("nb-results") nbResults: Int? = null
    ): Response<SpeciesIdentificationResponse>

    // ==================== Varieties Identification ====================

    /**
     * Get list of identifiable varieties
     * GET /v2/varieties
     *
     * @param apiKey PlantNet API key
     * @param prefix Filter varieties by name prefix
     * @param lang Language code
     */
    @GET("varieties")
    suspend fun getVarieties(

        @Query("prefix") prefix: String? = null,
        @Query("lang") lang: String? = null
    ): Response<List<VarietyInfo>>

    /**
     * Identify plant variety from images
     * POST /v2/varieties/identify
     *
     * @param apiKey PlantNet API key
     * @param images List of image files (max 5)
     * @param organs List of organ types corresponding to each image
     * @param lang Language code
     * @param includeRelatedImages Include similar images in results
     * @param noReject Don't reject non-plant images
     * @param nbResults Limit number of results
     */
    @Multipart
    @POST("varieties/identify")
    suspend fun identifyVariety(

        @Part images: List<MultipartBody.Part>,
        @Part("organs") organs: List<@JvmSuppressWildcards RequestBody>,
        @Query("lang") lang: String? = null,
        @Query("include-related-images") includeRelatedImages: Boolean? = null,
        @Query("no-reject") noReject: Boolean? = null,
        @Query("nb-results") nbResults: Int? = null
    ): Response<VarietyIdentificationResponse>

    // ==================== Diseases Identification ====================

    /**
     * Get list of identifiable diseases
     * GET /v2/diseases
     *
     * @param apiKey PlantNet API key
     * @param prefix Filter diseases by name or label prefix
     * @param lang Language code
     */
    @GET("diseases")
    suspend fun getDiseases(

        @Query("prefix") prefix: String? = null,
        @Query("lang") lang: String? = null
    ): Response<List<DiseaseInfo>>

    /**
     * Identify plant disease from images
     * POST /v2/diseases/identify
     *
     * @param apiKey PlantNet API key
     * @param images List of image files (max 5)
     * @param organs List of organ types corresponding to each image
     * @param lang Language code
     * @param includeRelatedImages Include similar images in results
     * @param noReject Don't reject non-plant images
     * @param nbResults Limit number of results
     */
    @Multipart
    @POST("diseases/identify")
    suspend fun identifyDisease(

        @Part images: List<MultipartBody.Part>,
        @Part("organs") organs: List<@JvmSuppressWildcards RequestBody>,
        @Query("lang") lang: String? = null,
        @Query("include-related-images") includeRelatedImages: Boolean? = null,
        @Query("no-reject") noReject: Boolean? = null,
        @Query("nb-results") nbResults: Int? = null
    ): Response<DiseaseIdentificationResponse>

    // ==================== Supporting Endpoints ====================

    /**
     * Get list of available projects/floras
     * GET /v2/projects
     */
    @GET("projects")
    suspend fun getProjects(

        @Query("lang") lang: String? = null
    ): Response<List<ProjectInfo>>

    /**
     * Get list of supported languages
     * GET /v2/languages
     */
    @GET("languages")
    suspend fun getLanguages(
        @Query("api-key") apiKey: String
    ): Response<List<LanguageInfo>>
}