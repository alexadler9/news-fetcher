package ru.alexadler9.newsfetcher.data.news

import ru.alexadler9.newsfetcher.data.local.model.BookmarkEntity
import ru.alexadler9.newsfetcher.data.remote.model.ArticleRemoteModel
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Mappers between remote/local news models and domain models.
 */

private const val DATE_TIME_PATTERN_DEFAULT = "dd.MM.yyyy HH:mm"
private const val DATE_TIME_PATTERN_ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'"

private fun convertDateISOToOtherFormat(
    date: String,
    pattern: String = DATE_TIME_PATTERN_DEFAULT
): String =
    SimpleDateFormat(pattern, Locale.getDefault())
        .format(SimpleDateFormat(DATE_TIME_PATTERN_ISO, Locale.getDefault()).parse(date)!!)

fun ArticleRemoteModel.toDomain() = ArticleModel(
    url = url,
    title = title,
    author = author ?: "",
    description = description ?: "",
    urlToImage = urlToImage ?: "",
    publishedAt = convertDateISOToOtherFormat(publishedAt)
)

fun BookmarkEntity.toDomain() = ArticleModel(
    url = url,
    title = title,
    author = author,
    description = description,
    urlToImage = urlToImage,
    publishedAt = publishedAt
)

fun ArticleModel.toEntity() = BookmarkEntity(
    url = url,
    title = title,
    author = author,
    description = description,
    urlToImage = urlToImage,
    publishedAt = publishedAt
)