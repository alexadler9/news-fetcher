package ru.alexadler9.newsfetcher.utility

import androidx.paging.LoadState
import androidx.paging.LoadStates
import ru.alexadler9.newsfetcher.data.news.local.db.model.BookmarkEntity
import ru.alexadler9.newsfetcher.data.news.remote.model.ArticleRemoteModel
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

val ARTICLE_REMOTE_MODEL_EMPTY_FIELDS = ArticleRemoteModel(
    author = null,
    title = "title",
    description = null,
    url = "https:/article-url",
    urlToImage = null,
    publishedAt = "2024-01-01T12:00:00Z"
)

val ARTICLE_LOCAL_MODEL = BookmarkEntity(
    url = "https:/article-url",
    title = "title",
    author = "author",
    description = "description",
    urlToImage = "https:/article-image-url",
    publishedAt = "2024-01-01T12:00:00Z"
)

val ARTICLE_MODEL_1 = ArticleModel(
    url = "https:/article-url-1",
    author = "author 1",
    title = "title 1",
    description = "description 1",
    urlToImage = "https:/article-image-url-1",
    publishedAt = "2024-01-01T12:00:00Z"
)

val ARTICLE_MODEL_2 = ArticleModel(
    url = "https:/article-url-2",
    author = "author 2",
    title = "title 2",
    description = "description 2",
    urlToImage = "https:/article-image-url-2",
    publishedAt = "2024-01-01T12:00:00Z"
)

val EXCEPTION_LOAD = RuntimeException("Failed to load")

val PAGER_STATES_LOADED = LoadStates(
    refresh = LoadState.NotLoading(true),
    prepend = LoadState.NotLoading(true),
    append = LoadState.NotLoading(true)
)

val PAGER_STATES_ERROR = LoadStates(
    refresh = LoadState.Error(EXCEPTION_LOAD),
    prepend = LoadState.NotLoading(true),
    append = LoadState.NotLoading(true)
)