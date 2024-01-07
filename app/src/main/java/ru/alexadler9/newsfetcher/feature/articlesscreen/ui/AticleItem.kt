package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.model.ArticleModel

data class ArticleItem(
    val data: ArticleModel,
    var bookmarked: Boolean
)