package ru.alexadler9.newsfetcher.data.remote

import ru.alexadler9.newsfetcher.data.remote.model.ArticlesRemoteModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRemoteSource @Inject constructor(private val api: NewsApi) {

    /**
     * Get live top articles headlines.
     */
    suspend fun getArticles(): ArticlesRemoteModel {
        return api.getArticles()
    }
}