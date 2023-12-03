package com.example.university.Model.API.Translation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Translation(
    @SerialName(value = "text")
    val text: String,

    @SerialName(value = "detectedLanguageCode")
    val detectedLanguageCode: String
)
