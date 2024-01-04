package ru.alexadler9.newsfetcher.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.alexadler9.newsfetcher.data.remote.model.ArticlesRemoteModel

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun getArticles(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = NEWS_API_KEY
    ): ArticlesRemoteModel
}