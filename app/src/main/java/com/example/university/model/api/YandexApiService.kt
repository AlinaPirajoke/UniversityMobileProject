package com.example.university.model.api

import com.example.university.model.api.translation.TranslationResponseList
import com.example.university.model.api.translation.TranslationRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface YandexApiService {

    @Headers("Content-Type: application/json")
    @POST("translate/v2/translate")
    suspend fun translateThis(
        @Body translateRequest: TranslationRequest,
        @Header("Authorization") apiKey: String,
    ): TranslationResponseList

    companion object {
        operator fun invoke(retrofitAdapter: RetrofitAdapter): YandexApiService {
            return retrofitAdapter.getInstance().create(YandexApiService::class.java)
        }
    }
}