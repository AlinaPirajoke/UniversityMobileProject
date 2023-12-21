package com.example.university.model.api.translation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslationResponse(
    @SerialName(value = "text")
    val text: String,

    @SerialName(value = "detectedLanguageCode")
    val detectedLanguageCode: String
)
