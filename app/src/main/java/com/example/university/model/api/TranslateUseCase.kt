package com.example.university.model.api

import android.content.Context
import com.example.university.R
import com.example.university.model.api.translation.TranslationRequest
import com.example.university.model.api.translation.TranslationResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class TranslateUseCase(context: Context) {
//    val Y_TRANS_API_KEY = "Api-Key AQVN3ks68mmMXJtkP6JL8trXKqDgYpNOfWeJ9K7Q"
    val Y_TRANS_API_KEY = context.applicationContext.resources.getString(R.string.Y_TRANS_API_KEY)
    val Y_TRANS_API_BASE_URL = "https://translate.api.cloud.yandex.net"
    val retrofitAdapter = RetrofitAdapter(getClient(), Y_TRANS_API_BASE_URL)
    val yApi = YandexApiService.invoke(retrofitAdapter)

    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        return client
    }

    suspend operator fun invoke(text: String, targetLang: String = "ru"): TranslationResponse {
        val translationRequest =
            TranslationRequest(targetLanguageCode = targetLang, texts = listOf(text))
        val translation =
            yApi.translateThis(translateRequest = translationRequest, apiKey = Y_TRANS_API_KEY)
        return translation.translations[0]
    }
}