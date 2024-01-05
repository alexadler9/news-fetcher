package ru.alexadler9.newsfetcher.data

import ru.alexadler9.newsfetcher.data.remote.NewsRemoteSource
import ru.alexadler9.newsfetcher.domain.model.ArticleModel

class NewsRepositoryImpl(private val source: NewsRemoteSource) : NewsRepository {

    override suspend fun getArticles(): List<ArticleModel> {
        return source.getArticles().articleList.map {
            it.toDomain()
        }
    }
}