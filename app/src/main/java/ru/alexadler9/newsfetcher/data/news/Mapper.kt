package ru.alexadler9.newsfetcher.data.news

import ru.alexadler9.newsfetcher.data.news.local.db.model.BookmarkEntity
import ru.alexadler9.newsfetcher.data.news.remote.model.ArticleRemoteModel
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Mappers between remote/local news models and domain models.
 */

private const val DT_PATTERN_SERVER = "yyyy-MM-dd'T'HH:mm:ss'Z'"    // ISO
private const val TIME_ZONE_SERVER = "UTC"

fun convertDateToLocal(date: String, pattern: String): String {
    val dfFrom = SimpleDateFormat(DT_PATTERN_SERVER, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone(TIME_ZONE_SERVER)
    }
    val dfTo = SimpleDateFormat(pattern, Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    return dfTo.format(dfFrom.parse(date)!!)
}

fun ArticleRemoteModel.toDomain() = ArticleModel(
    url = url,
    title = title,
    author = author ?: "",
    description = description ?: "",
    urlToImage = urlToImage ?: "",
    publishedAt = publishedAt
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