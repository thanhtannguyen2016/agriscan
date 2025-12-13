package com.agri.agriscan.data.repository


import com.agri.agriscan.BuildConfig
import com.agri.agriscan.data.remote.api.OpenAIApi
import com.agri.agriscan.data.remote.dto.openai.ChatMessage
import com.agri.agriscan.data.remote.dto.openai.ChatRequest
import com.agri.agriscan.data.remote.dto.openai.TreatmentResponse
import com.agri.agriscan.data.mapper.TreatmentMapper
import com.agri.agriscan.domain.model.*
import com.agri.agriscan.domain.repository.TreatmentRepository
import com.agri.agriscan.util.Constants
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TreatmentRepositoryImpl @Inject constructor(
    private val openAIApi: OpenAIApi,
    private val treatmentMapper: TreatmentMapper,
    private val gson: Gson
) : TreatmentRepository {

    override suspend fun getTreatment(
        plant: Plant,
        disease: Disease
    ): Flow<Resource<Treatment>> = flow {
        try {
            emit(Resource.Loading)

            // Build prompt for GPT-4
            val prompt = Constants.getTreatmentPrompt(
                plantName = plant.commonNames.firstOrNull() ?: plant.scientificName,
                diseaseName = disease.name,
                diseaseDescription = disease.description ?: "Không có mô tả"
            )

            // Create chat request
            val chatRequest = ChatRequest(
                model = Constants.GPT_MODEL,
                messages = listOf(
                    ChatMessage(
                        role = "system",
                        content = "Bạn là một chuyên gia nông nghiệp chuyên về bệnh cây trồng và phương pháp điều trị."
                    ),
                    ChatMessage(
                        role = "user",
                        content = prompt
                    )
                ),
                temperature = Constants.TEMPERATURE,
                maxTokens = Constants.MAX_TOKENS
            )

            // Call OpenAI API
            val response = openAIApi.createChatCompletion(
                authorization = "Bearer ${BuildConfig.OPENAI_API_KEY}",
                request = chatRequest
            )

            if (response.isSuccessful && response.body() != null) {
                val chatResponse = response.body()!!

                if (chatResponse.choices.isNotEmpty()) {
                    val content = chatResponse.choices.first().message.content

                    try {
                        // Parse JSON response
                        val treatmentResponse = gson.fromJson(
                            content,
                            TreatmentResponse::class.java
                        )

                        // Map to domain model
                        val treatment = treatmentMapper.mapToTreatment(
                            treatmentResponse,
                            plant,
                            disease
                        )

                        emit(Resource.Success(treatment))
                    } catch (e: Exception) {
                        // If JSON parsing fails, try to extract useful information
                        emit(Resource.Error("Không thể phân tích phản hồi từ GPT-4: ${e.message}"))
                    }
                } else {
                    emit(Resource.Error("Không nhận được phản hồi từ GPT-4"))
                }
            } else {
                emit(Resource.Error("Lỗi API: ${response.code()} - ${response.message()}"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("Lỗi: ${e.message}", e))
        }
    }
}