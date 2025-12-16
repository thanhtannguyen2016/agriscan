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
                        content = "Bạn là một chuyên gia nông nghiệp chuyên về bệnh cây trồng và phương pháp điều trị. Bạn PHẢI trả về JSON object hợp lệ, bắt đầu với { và kết thúc với }. KHÔNG thêm markdown, KHÔNG thêm giải thích."
                    ),
                    ChatMessage(
                        role = "user",
                        content = prompt
                    )
                ),
                temperature = Constants.TEMPERATURE,
                maxTokens = Constants.MAX_TOKENS
                // Note: response_format requires special Gson naming strategy
                // For now, rely on prompt engineering and response cleaning
            )

            // Call OpenAI API
            val response = openAIApi.createChatCompletion(
                authorization = "Bearer ${BuildConfig.OPENAI_API_KEY}",
                request = chatRequest
            )

            if (response.isSuccessful && response.body() != null) {
                val chatResponse = response.body()!!

                if (chatResponse.choices.isNotEmpty()) {
                    var content = chatResponse.choices.first().message.content

                    try {
                        // Clean up response - remove markdown code blocks if present
                        content = content.trim()

                        // Remove ```json and ``` if present
                        if (content.startsWith("```json")) {
                            content = content.removePrefix("```json").removeSuffix("```").trim()
                        } else if (content.startsWith("```")) {
                            content = content.removePrefix("```").removeSuffix("```").trim()
                        }

                        // Find JSON object if there's text before/after
                        val jsonStart = content.indexOf('{')
                        val jsonEnd = content.lastIndexOf('}')

                        if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                            content = content.substring(jsonStart, jsonEnd + 1)
                        }

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
                        // Log the actual response for debugging
                        android.util.Log.e("TreatmentRepository", "Failed to parse response: $content")
                        emit(Resource.Error("Không thể phân tích phản hồi từ GPT-4. Vui lòng thử lại."))
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