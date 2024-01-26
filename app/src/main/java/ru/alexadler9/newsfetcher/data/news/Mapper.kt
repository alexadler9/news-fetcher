package ru.alexadler9.newsfetcher.data.news

import ru.alexadler9.newsfetcher.data.news.local.db.model.BookmarkEntity
import ru.alexadler9.newsfetcher.data.news.remote.model.ArticleRemoteModel
import ru.alexadler9.newsfetcher.data.news.remote.type.ArticlesCategoryRemote
import ru.alexadler9.newsfetcher.data.news.remote.type.ArticlesCountryRemote
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry
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

fun ArticlesCountry.toRemote(): ArticlesCountryRemote =
    when (this) {
        ArticlesCountry.USA -> ArticlesCountryRemote.us
        ArticlesCountry.RUSSIA -> ArticlesCountryRemote.ru
    }

fun ArticlesCategory.toRemote(): ArticlesCategoryRemote =
    when (this) {
        ArticlesCategory.GENERAL -> ArticlesCategoryRemote.general
        ArticlesCategory.BUSINESS -> ArticlesCategoryRemote.business
        ArticlesCategory.SCIENCE -> ArticlesCategoryRemote.science
        ArticlesCategory.HEALTH -> ArticlesCategoryRemote.health
        ArticlesCategory.SPORTS -> ArticlesCategoryRemote.sports
        ArticlesCategory.ENTERTAINMENT -> ArticlesCategoryRemote.entertainment
        ArticlesCategory.TECHNOLOGY -> ArticlesCategoryRemote.technology
    }