package ru.alexadler9.newsfetcher.data.news.remote

import androidx.annotation.IntRange
import retrofit2.http.GET
import retrofit2.http.Query
import ru.alexadler9.newsfetcher.BuildConfig
import ru.alexadler9.newsfetcher.data.news.remote.model.ArticlesRemoteModel
import ru.alexadler9.newsfetcher.data.news.remote.type.ArticlesCategoryRemote
import ru.alexadler9.newsfetcher.data.news.remote.type.ArticlesCountryRemote

interface NewsApi {

    /**
     * Get live top articles headlines.
     * @param apiKey Unique API key.
     * @param country The 2-letter ISO 3166-1 code of the country you want to get headlines for.
     * @param category The category you want to get headlines for.
     * @param query Keywords or a phrase to search for.
     * @param pageSize The number of results to return per page (request).
     * @param page Use this to page through the results.
     */
    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesArticles(
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
        @Query("country") country: ArticlesCountryRemote = ArticlesCountryRemote.us,
        @Query("category") category: ArticlesCategoryRemote = ArticlesCategoryRemote.general,
        @Query("q") query: String = "",
        @Query("pageSize") @IntRange(
            from = 1,
            to = PAGE_SIZE_MAX.toLong()
        ) pageSize: Int = PAGE_SIZE_DEFAULT,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): ArticlesRemoteModel

    companion object {

        const val PAGE_SIZE_DEFAULT = 20
        const val PAGE_SIZE_MAX = 20
    }
}