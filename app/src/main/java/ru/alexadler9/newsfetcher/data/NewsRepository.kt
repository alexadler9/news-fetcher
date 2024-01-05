package ru.alexadler9.newsfetcher.data

import ru.alexadler9.newsfetcher.data.remote.NewsRemoteSource
import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.model.ArticleModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(private val source: NewsRemoteSource) {

    /**
     * Get live top articles headlines.
     */
    suspend fun getArticles(): List<ArticleModel> {
        return source.getArticles().articleList.map {
            it.toDomain()
        }
    }
}