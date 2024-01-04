package ru.alexadler9.newsfetcher.data

import ru.alexadler9.newsfetcher.data.remote.NewsRemoteSource
import ru.alexadler9.newsfetcher.data.remote.model.ArticlesRemoteModel

class NewsRepositoryImpl(private val source: NewsRemoteSource) : NewsRepository {

    override suspend fun getArticles(): ArticlesRemoteModel {
        return source.getArticles()
    }
}