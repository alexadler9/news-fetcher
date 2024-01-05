package ru.alexadler9.newsfetcher.data.remote

import ru.alexadler9.newsfetcher.data.remote.model.ArticlesRemoteModel

class NewsRemoteSource(private val api: NewsService) {

    /**
     * Get live top articles headlines.
     */
    suspend fun getArticles(): ArticlesRemoteModel {
        return api.getArticles()
    }
}