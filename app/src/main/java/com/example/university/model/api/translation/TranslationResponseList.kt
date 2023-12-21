package com.example.university.model.api.translation

import kotlinx.serialization.Serializable

@Serializable
data class TranslationResponseList(
    val translations: List<TranslationResponse>
)