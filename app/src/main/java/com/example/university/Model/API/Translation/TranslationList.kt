package com.example.university.Model.API.Translation

import kotlinx.serialization.Serializable

@Serializable
data class TranslationList(
    val translations: List<Translation>
)