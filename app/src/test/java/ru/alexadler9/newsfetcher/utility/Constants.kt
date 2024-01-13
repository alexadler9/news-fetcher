package ru.alexadler9.newsfetcher.utility

import ru.alexadler9.newsfetcher.data.remote.model.ArticleRemoteModel
import ru.alexadler9.newsfetcher.data.remote.model.ArticlesRemoteModel
import ru.alexadler9.newsfetcher.domain.model.ArticleModel

val ARTICLE_REMOTE_MODEL_1 = ArticleRemoteModel(
    author = "author 1",
    title = "title 1",
    description = "description 1",
    url = "https:/article-url-1",
    urlToImage = "https:/article-image-url-1",
    publishedAt = "2024-01-01T12:00:00Z"
)

val ARTICLE_REMOTE_MODEL_2 = ArticleRemoteModel(
    author = "author 2",
    title = "title 2",
    description = "description 2",
    url = "https:/article-url-2",
    urlToImage = "https:/article-image-url-2",
    publishedAt = "2024-01-01T12:00:00Z"
)

val ARTICLES_REMOTE_MODEL = ArticlesRemoteModel(
    articleList = listOf(
        ARTICLE_REMOTE_MODEL_1,
        ARTICLE_REMOTE_MODEL_2
    )
)

val ARTICLE_MODEL_1 = ArticleModel(
    url = "https:/article-url-1",
    author = "author 1",
    title = "title 1",
    description = "description 1",
    urlToImage = "https:/article-image-url-1",
    publishedAt = "01.01.2024 12:00"
)

val ARTICLE_MODEL_2 = ArticleModel(
    url = "https:/article-url-2",
    author = "author 2",
    title = "title 2",
    description = "description 2",
    urlToImage = "https:/article-image-url-2",
    publishedAt = "01.01.2024 12:00"
)