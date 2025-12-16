package com.agri.agriscan.data.remote.dto.openai

import com.google.gson.annotations.SerializedName

data class ResponseFormat(
    @SerializedName("type")
    val type: String // "json_object" or "text"
)