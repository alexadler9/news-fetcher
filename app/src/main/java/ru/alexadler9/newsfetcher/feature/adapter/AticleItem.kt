package ru.alexadler9.newsfetcher.feature.adapter

import ru.alexadler9.newsfetcher.domain.model.ArticleModel

data class ArticleItem(
    val data: ArticleModel,
    var bookmarked: Boolean
)