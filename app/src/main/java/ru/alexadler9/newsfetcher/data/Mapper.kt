package ru.alexadler9.newsfetcher.data

import ru.alexadler9.newsfetcher.data.remote.model.ArticleRemoteModel
import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.model.ArticleModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Mappers from remote news models to local models.
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
    title = title,
    author = author ?: "",
    description = description ?: "",
    publishedAt = convertDateISOToOtherFormat(publishedAt)
)