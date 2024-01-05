package ru.alexadler9.newsfetcher.data

import ru.alexadler9.newsfetcher.domain.model.ArticleModel

interface NewsRepository {

    /**
     * Get live top articles headlines.
     */
    suspend fun getArticles(): List<ArticleModel>
}