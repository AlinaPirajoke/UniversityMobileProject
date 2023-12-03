package com.example.university.Model.API.Translation

import kotlinx.serialization.Serializable

@Serializable
data class TranslationRequest (
    val targetLanguageCode: String,
    val texts: List<String>
)
