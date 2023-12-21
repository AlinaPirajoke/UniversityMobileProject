package com.example.university.model.api.translation

import kotlinx.serialization.Serializable

@Serializable
data class TranslationRequest (
    val targetLanguageCode: String,
    val texts: List<String>
)
