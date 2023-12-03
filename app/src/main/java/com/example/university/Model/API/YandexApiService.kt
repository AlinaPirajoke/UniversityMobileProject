package com.example.university.Model.API

import com.example.university.Model.API.Translation.TranslationList
import com.example.university.Model.API.Translation.TranslationRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface YandexApiService {

    @Headers("Content-Type: application/json")
    @POST("translate/v2/translate")
    suspend fun translateThis(
        @Body translateRequest: TranslationRequest,
        @Header("Authorization") apiKey: String
    ): TranslationList
}