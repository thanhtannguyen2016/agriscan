package com.agri.agriscan.data.remote.dto.openai
import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("model")
    val model: String,

    @SerializedName("messages")
    val messages: List<ChatMessage>,

    @SerializedName("temperature")
    val temperature: Float? = null,

    @SerializedName("max_tokens")
    val maxTokens: Int? = null,

    @SerializedName("top_p")
    val topP: Float? = null,

    @SerializedName("frequency_penalty")
    val frequencyPenalty: Float? = null,

    @SerializedName("presence_penalty")
    val presencePenalty: Float? = null,
)

data class ChatMessage(
    @SerializedName("role")
    val role: String, // "system", "user", "assistant"

    @SerializedName("content")
    val content: String
)