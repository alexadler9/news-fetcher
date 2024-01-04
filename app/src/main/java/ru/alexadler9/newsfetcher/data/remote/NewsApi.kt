package ru.alexadler9.newsfetcher.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val NEWS_BASE_URL = "https://newsapi.org/"
const val NEWS_API_KEY = "0806d961609147c0a84f3c3de3c9b67b"

class NewsApi {

    companion object {
        @Volatile
        private var INSTANCE: NewsService? = null
        fun getInstance(): NewsService {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Retrofit.Builder()
                        .baseUrl(NEWS_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(OkHttpClient.Builder().build())
                        .build()
                        .create(NewsService::class.java)
                }
                return instance!!
            }
        }
    }
}