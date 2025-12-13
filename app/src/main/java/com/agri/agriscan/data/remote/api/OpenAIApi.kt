package com.agri.agriscan.data.remote.api

import com.agri.agriscan.data.remote.dto.openai.ChatRequest
import com.agri.agriscan.data.remote.dto.openai.ChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * OpenAI API Interface
 * Base URL: https://api.openai.com/v1/
 */
interface OpenAIApi {

    /**
     * Create chat completion
     * POST /v1/chat/completions
     *
     * @param authorization Bearer token (API key)
     * @param request Chat request with messages and model
     */
    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatRequest
    ): Response<ChatResponse>
}