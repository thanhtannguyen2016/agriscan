package com.agri.agriscan.util

// Constants.kt
object Constants {
    // API Base URLs

    // Endpoints
    const val ENDPOINT_IDENTIFY = "identify/{project}"
    const val ENDPOINT_DISEASES = "diseases/{project}"  // Cần xác nhận endpoint chính xác
    const val ENDPOINT_VARIETIES = "varieties"          // Cần xác nhận endpoint chính xác

    // Thresholds
    const val MAX_RESULTS = 10

    // Image
    const val MAX_IMAGE_SIZE = 2048

    // ==================== API Base URLs ====================
    const val PLANTNET_BASE_URL = "https://my-api.plantnet.org/v2/"
    const val OPENAI_BASE_URL = "https://api.openai.com/v1/"

    // ==================== PlantNet Endpoints ====================
    // Species identification
    const val ENDPOINT_IDENTIFY_SPECIES = "identify/{project}"

    // Varieties identification
    const val ENDPOINT_GET_VARIETIES = "varieties"
    const val ENDPOINT_IDENTIFY_VARIETY = "varieties/identify"

    // Diseases identification
    const val ENDPOINT_GET_DISEASES = "diseases"
    const val ENDPOINT_IDENTIFY_DISEASE = "diseases/identify"

    // Other endpoints
    const val ENDPOINT_PROJECTS = "projects"
    const val ENDPOINT_LANGUAGES = "languages"

    // ==================== OpenAI Endpoints ====================
    const val ENDPOINT_CHAT_COMPLETIONS = "chat/completions"

    // ==================== PlantNet Parameters ====================
    // Default project for identification
    const val DEFAULT_PROJECT = "all"

    // Organ types
    const val ORGAN_AUTO = "auto"
    const val ORGAN_LEAF = "leaf"
    const val ORGAN_FLOWER = "flower"
    const val ORGAN_FRUIT = "fruit"
    const val ORGAN_BARK = "bark"

    // Language codes
    const val LANG_ENGLISH = "en"
    const val LANG_VIETNAMESE = "vi"
    const val LANG_FRENCH = "fr"

    // ==================== Confidence Thresholds ====================
    // Minimum confidence score to auto-proceed to disease identification
    const val CONFIDENCE_THRESHOLD = 0.5f

    // Minimum confidence to show result
    const val MIN_CONFIDENCE = 0.01f

    // ==================== Result Limits ====================
    const val MAX_RESULTS_SPECIES = 10
    const val MAX_RESULTS_VARIETY = 10
    const val MAX_RESULTS_DISEASE = 10

    // ==================== Image Settings ====================
    // Maximum image dimensions
    const val MAX_IMAGE_WIDTH = 2048
    const val MAX_IMAGE_HEIGHT = 2048

    // Image quality (0-100)
    const val IMAGE_QUALITY = 85

    // Maximum images per request
    const val MAX_IMAGES_PER_REQUEST = 5

    // Maximum total POST size in MB
    const val MAX_POST_SIZE_MB = 50

    // ==================== OpenAI Settings ====================
    const val GPT_MODEL = "gpt-4o"
    const val MAX_TOKENS = 1500
    const val TEMPERATURE = 0.7f

    // ==================== Database Settings ====================
    const val DATABASE_NAME = "agriscan_database"
    const val DATABASE_VERSION = 1

    // ==================== SharedPreferences ====================
    const val PREFS_NAME = "agriscan_prefs"
    const val PREF_API_KEY_PLANTNET = "api_key_plantnet"
    const val PREF_API_KEY_OPENAI = "api_key_openai"
    const val PREF_LANGUAGE = "language"
    const val PREF_FIRST_RUN = "first_run"

    // ==================== Network Settings ====================
    const val NETWORK_TIMEOUT_SECONDS = 30L
    const val NETWORK_READ_TIMEOUT_SECONDS = 30L
    const val NETWORK_WRITE_TIMEOUT_SECONDS = 30L

    // ==================== Intent Extras ====================
    const val EXTRA_IMAGE_URI = "extra_image_uri"
    const val EXTRA_PLANT_DATA = "extra_plant_data"
    const val EXTRA_DISEASE_DATA = "extra_disease_data"
    const val EXTRA_VARIETY_DATA = "extra_variety_data"

    // ==================== Request Codes ====================
    const val REQUEST_CODE_CAMERA = 1001
    const val REQUEST_CODE_GALLERY = 1002
    const val REQUEST_CODE_PERMISSIONS = 1003

    // ==================== Error Messages ====================
    const val ERROR_NO_INTERNET = "Không có kết nối internet"
    const val ERROR_TIMEOUT = "Yêu cầu hết thời gian chờ"
    const val ERROR_UNKNOWN = "Đã xảy ra lỗi không xác định"
    const val ERROR_API_KEY_MISSING = "Thiếu API key"
    const val ERROR_IMAGE_TOO_LARGE = "Ảnh quá lớn"
    const val ERROR_INVALID_IMAGE = "Ảnh không hợp lệ"
    const val ERROR_NO_RESULTS = "Không tìm thấy kết quả"

    // ==================== Prompts for OpenAI ====================
    fun getTreatmentPrompt(plantName: String?, diseaseName: String, diseaseDescription: String): String {
        return """
            Bạn là một chuyên gia nông nghiệp. Hãy cung cấp phương pháp điều trị chi tiết cho bệnh sau:
            
            Cây trồng: $plantName
            Bệnh: $diseaseName
            Mô tả: $diseaseDescription
            
            Vui lòng cung cấp thông tin theo định dạng JSON sau:
            {
              "chemicalTreatments": [
                {
                  "name": "Tên thuốc hóa học",
                  "activeIngredients": ["Hoạt chất 1", "Hoạt chất 2"],
                  "dosage": "Liều lượng khuyến nghị",
                  "usage": "Cách sử dụng chi tiết",
                  "precautions": "Lưu ý an toàn"
                }
              ],
              "biologicalTreatments": [
                {
                  "name": "Tên phương pháp sinh học",
                  "description": "Mô tả chi tiết",
                  "materials": ["Nguyên liệu 1", "Nguyên liệu 2"],
                  "steps": ["Bước 1", "Bước 2", "Bước 3"],
                  "effectiveness": "Hiệu quả"
                }
              ],
              "prevention": {
                "title": "Biện pháp phòng ngừa",
                "tips": ["Mẹo 1", "Mẹo 2", "Mẹo 3"]
              },
              "generalAdvice": "Lời khuyên chung về xử lý bệnh này"
            }
            
            Chỉ trả về JSON, không thêm giải thích nào khác.
        """.trimIndent()
    }

    // ==================== Date Formats ====================
    const val DATE_FORMAT_DISPLAY = "dd/MM/yyyy HH:mm"
    const val DATE_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    // ==================== Notification ====================
    const val NOTIFICATION_CHANNEL_ID = "agriscan_channel"
    const val NOTIFICATION_CHANNEL_NAME = "AgriScan Notifications"
}