package com.example.university.model.api

import android.provider.Settings.Global.getString
import com.example.university.model.api.translation.TranslationResponse
import com.example.university.model.api.translation.TranslationRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.compose.inject
import org.koin.java.KoinJavaComponent.inject

class TranslateUseCase {
//    val Y_TRANS_API_KEY = "Api-Key AQVN3ks68mmMXJtkP6JL8trXKqDgYpNOfWeJ9K7Q"
    val Y_TRANS_API_KEY = BuildConfig.Y_TRANS_API_KEY
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