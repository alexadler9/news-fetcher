package ru.alexadler9.newsfetcher.data

import ru.alexadler9.newsfetcher.data.remote.model.ArticleRemoteModel
import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.model.ArticleModel

/**
 * Mappers from remote news models to local models.
 */

fun ArticleRemoteModel.toDomain() = ArticleModel(
    title = title,
    author = author ?: "",
    description = description ?: "",
    publishedAt = publishedAt
)