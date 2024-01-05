package ru.alexadler9.newsfetcher.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.alexadler9.newsfetcher.data.remote.NewsApi
import ru.alexadler9.newsfetcher.di.scopes.AppScope

private const val NEWS_BASE_URL = "https://newsapi.org/"
const val NEWS_API_KEY = "0806d961609147c0a84f3c3de3c9b67b"

@Module
class NetworkModule {

    @AppScope
    @Provides
    fun provideApi(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @AppScope
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @AppScope
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}