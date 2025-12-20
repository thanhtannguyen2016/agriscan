package com.agri.agriscan.data.repository

import android.content.Context
import android.net.Uri
import com.agri.agriscan.BuildConfig
import com.agri.agriscan.data.local.database.dao.HistoryDao
import com.agri.agriscan.data.remote.api.PlantNetApi
import com.agri.agriscan.data.mapper.DiseaseMapper
import com.agri.agriscan.domain.model.*
import com.agri.agriscan.domain.repository.DiseaseRepository
import com.agri.agriscan.util.Constants
import com.agri.agriscan.util.ImageUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.map

class DiseaseRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val plantNetApi: PlantNetApi,
    private val diseaseMapper: DiseaseMapper,
    private val historyDao: HistoryDao
) : DiseaseRepository {

    override suspend fun getDiseases(prefix: String?): Flow<Resource<List<Disease>>> = flow {
        try {
            emit(Resource.Loading)

            val response = plantNetApi.getDiseases(
                prefix = prefix,
                lang = Constants.LANG_VIETNAMESE
            )

            if (response.isSuccessful && response.body() != null) {
                val diseases = response.body()!!.map { diseaseMapper.mapToDisease(it) }
                emit(Resource.Success(diseases))
            } else {
                emit(Resource.Error("Lỗi API: ${response.code()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("Lỗi: ${e.message}", e))
        }
    }

    override suspend fun identifyDisease(
        plant: Plant,
        imageUris: List<String>,
        organs: List<String>
    ): Flow<Resource<DiseaseIdentification>> = flow {
        try {
            emit(Resource.Loading)

            // Prepare images
            val imageParts = prepareImageParts(imageUris)
            if (imageParts.isEmpty()) {
                emit(Resource.Error("Không thể xử lý ảnh"))
                return@flow
            }

            // Prepare organs
            val organParts = organs.map { organ ->
                organ.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            // Call API
            val response = plantNetApi.identifyDisease(
                images = imageParts,
                organs = organParts,
                lang = Constants.LANG_VIETNAMESE,
                includeRelatedImages = true,
                nbResults = Constants.MAX_RESULTS_DISEASE
            )

            if (response.isSuccessful && response.body() != null) {
                val result = diseaseMapper.mapToDiseaseIdentification(
                    response.body()!!,
                    imageUris.first()
                )
                emit(Resource.Success(result))
            } else {
                emit(Resource.Error("Lỗi API: ${response.code()} - ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("Lỗi: ${e.message}", e))
        }
    }

    /**
     * Prepare image multipart parts from URIs
     */
    private fun prepareImageParts(imageUris: List<String>): List<MultipartBody.Part> {
        val parts = mutableListOf<MultipartBody.Part>()

        imageUris.take(Constants.MAX_IMAGES_PER_REQUEST).forEach { uriString ->
            try {
                val uri = Uri.parse(uriString)
                val bitmap = ImageUtils.resizeImage(context, uri)

                if (bitmap != null) {
                    val file = ImageUtils.saveBitmapToFile(
                        context,
                        bitmap,
                        "disease_temp_${System.currentTimeMillis()}.jpg"
                    )

                    if (file != null && ImageUtils.getFileSizeInMB(file) < Constants.MAX_POST_SIZE_MB) {
                        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val part = MultipartBody.Part.createFormData(
                            "images",
                            file.name,
                            requestBody
                        )
                        parts.add(part)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return parts
    }
}