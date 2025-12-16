package com.agri.agriscan.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Objects cho OpenAI API
 */

/**
 * Chat Completion Request
 */
data class ChatCompletionRequest(
    @SerializedName("model")
    val model: String = "gpt-4o", // GPT-4o model

    @SerializedName("messages")
    val messages: List<ChatMessage>,

    @SerializedName("temperature")
    val temperature: Float = 0.7f,

    @SerializedName("max_tokens")
    val maxTokens: Int = 2000,

    @SerializedName("top_p")
    val topP: Float = 1.0f,

    @SerializedName("frequency_penalty")
    val frequencyPenalty: Float = 0.0f,

    @SerializedName("presence_penalty")
    val presencePenalty: Float = 0.0f
)


/**
 * Chat Message
 */
data class ChatMessage(
    @SerializedName("role")
    val role: String, // "system", "user", or "assistant"

    @SerializedName("content")
    val content: Any // String hoặc List<ContentPart> cho multimodal
)

/**
 * Content Part cho multimodal (text + image)
 */
data class ContentPart(
    @SerializedName("type")
    val type: String, // "text" or "image_url"

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("image_url")
    val imageUrl: ImageUrl? = null
)


/**
 * Chat Completion Response
 */
data class ChatCompletionResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("object")
    val objectType: String,

    @SerializedName("created")
    val created: Long,

    @SerializedName("model")
    val model: String,

    @SerializedName("choices")
    val choices: List<Choice>,

    @SerializedName("usage")
    val usage: Usage?
)

/**
 * Choice in response
 */
data class Choice(
    @SerializedName("index")
    val index: Int,

    @SerializedName("message")
    val message: ChatMessage,

    @SerializedName("finish_reason")
    val finishReason: String?
)

/**
 * Token usage information
 */
data class Usage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int,

    @SerializedName("completion_tokens")
    val completionTokens: Int,

    @SerializedName("total_tokens")
    val totalTokens: Int
)

/**
 * Helper để tạo system prompt cho disease identification
 */
object DiseaseIdentificationPrompt {
    fun createSystemPrompt(): String = """
        Bạn là chuyên gia về bệnh cây trồng. Nhiệm vụ của bạn là:
        1. Phân tích hình ảnh cây trồng và nhận dạng các dấu hiệu bệnh
        2. Xác định tên bệnh (tiếng Việt và khoa học)
        3. Mô tả triệu chứng, nguyên nhân, mức độ nghiêm trọng
        4. Đưa ra danh sách các bệnh có khả năng với xác suất
        
        Trả về kết quả dạng JSON với cấu trúc:
        {
            "diseases": [
                {
                    "name": "Tên bệnh tiếng Việt",
                    "scientific_name": "Tên khoa học",
                    "probability": 0.85,
                    "description": "Mô tả bệnh",
                    "symptoms": ["Triệu chứng 1", "Triệu chứng 2"],
                    "causes": ["Nguyên nhân 1", "Nguyên nhân 2"],
                    "severity": "HIGH|MEDIUM|LOW|CRITICAL"
                }
            ],
            "confidence": 0.85
        }
    """.trimIndent()

    fun createUserPrompt(plantName: String, imageBase64: String): List<ContentPart> {
        return listOf(
            ContentPart(
                type = "text",
                text = "Đây là hình ảnh của cây $plantName. Hãy phân tích và nhận dạng bệnh nếu có."
            ),
            ContentPart(
                type = "image_url",
                imageUrl = ImageUrl(
                    url = "data:image/jpeg;base64,$imageBase64",
                    detail = "high"
                )
            )
        )
    }
}

data class ImageUrl(
    @SerializedName("url")
    val url: String, // URL hoặc base64

    @SerializedName("detail")
    val detail: String? = "auto" // "low", "high", "auto"
)

/**
 * Helper để tạo prompt cho treatment recommendation
 */
object TreatmentPrompt {
    fun createSystemPrompt(): String = """
        Bạn là chuyên gia về điều trị bệnh cây trồng. Nhiệm vụ của bạn là:
        1. Đề xuất phương pháp điều trị hóa học (thuốc trừ bệnh)
        2. Đề xuất phương pháp điều trị sinh học (không dùng hóa chất)
        3. Đưa ra biện pháp phòng ngừa
        
        Trả về JSON với cấu trúc:
        {
            "chemical_treatments": [
                {
                    "product_name": "Tên thuốc",
                    "active_ingredient": "Hoạt chất",
                    "concentration": "Nồng độ",
                    "dosage": "Liều lượng",
                    "application_method": "Phương pháp",
                    "frequency": "Tần suất",
                    "safety_period": "Thời gian cách ly",
                    "precautions": ["Biện pháp an toàn"],
                    "manufacturer": "Nhà sản xuất"
                }
            ],
            "biological_treatments": [
                {
                    "name": "Tên phương pháp",
                    "description": "Mô tả",
                    "materials": ["Nguyên liệu"],
                    "steps": ["Bước thực hiện"],
                    "effectiveness": "Hiệu quả",
                    "duration": "Thời gian"
                }
            ],
            "prevention_measures": ["Biện pháp phòng ngừa"]
        }
    """.trimIndent()

    fun createUserPrompt(diseaseName: String, plantName: String, severity: String): String {
        return """
            Cây: $plantName
            Bệnh: $diseaseName
            Mức độ: $severity
            
            Hãy đề xuất phương pháp điều trị chi tiết cho bệnh này.
            Ưu tiên các phương pháp an toàn, hiệu quả và phù hợp với Việt Nam.
        """.trimIndent()
    }


}