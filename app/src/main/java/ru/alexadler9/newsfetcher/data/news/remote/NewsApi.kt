package ru.alexadler9.newsfetcher.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.alexadler9.newsfetcher.BuildConfig
import ru.alexadler9.newsfetcher.data.remote.model.ArticlesRemoteModel

interface NewsApi {

    /**
     * Get live top articles headlines.
     * @param country The 2-letter ISO 3166-1 code of the country you want to get headlines for.
     * @param apiKey Unique API key.
     */
    @GET("v2/top-headlines")
    suspend fun getArticles(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): ArticlesRemoteModel
}