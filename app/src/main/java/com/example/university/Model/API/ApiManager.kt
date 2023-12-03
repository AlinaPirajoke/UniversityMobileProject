package com.example.university.Model.API

import android.util.Log
import com.example.university.Model.API.Translation.Translation
import com.example.university.Model.API.Translation.TranslationRequest
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

private const val BASE_URL = "https://translate.api.cloud.yandex.net"
private const val API_KEY = "Api-Key AQVN3ks68mmMXJtkP6JL8trXKqDgYpNOfWeJ9K7Q"


class ApiManager {
    private val TAG = "ApiManager"
    private lateinit var retrofit: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(client).build()
    }

    suspend fun translateText(text: String): Translation {
        Log.d(TAG, "Я здесь")
        val yandexApi = retrofit.create(YandexApiService::class.java)
        Log.d(TAG, "А теперь я здесь")
        val response = yandexApi.translateThis(
            translateRequest = TranslationRequest(
                targetLanguageCode = "ru",
                texts = listOf(text),
            ),
            apiKey = API_KEY
        )
        Log.d(TAG, "Получен перевод слова $text: ${response.translations.joinToString()}")
        // Мы собираемся переводить не больше одного слова (на что яндекс тоже возвращает список из одного слова)
        return response.translations[0]
    }
}