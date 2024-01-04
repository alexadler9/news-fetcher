package ru.alexadler9.newsfetcher.data

import ru.alexadler9.newsfetcher.data.remote.model.ArticlesRemoteModel

interface NewsRepository {

    suspend fun getArticles(): ArticlesRemoteModel
}