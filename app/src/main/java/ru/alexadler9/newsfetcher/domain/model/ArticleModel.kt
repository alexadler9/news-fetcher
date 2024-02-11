package ru.alexadler9.newsfetcher.domain.model

import ru.alexadler9.newsfetcher.data.news.convertDateToLocal
import java.io.Serializable

data class ArticleModel(

    /** The direct URL to the article. */
    val url: String = "",

    /** The author of the article. */
    val author: String = "",

    /** The headline or title of the article. */
    val title: String = "",

    /** A description or snippet from the article. */
    val description: String = "",

    /** The URL to a relevant image for the article. */
    val urlToImage: String = "",

    /** The date and time that the article was published (yyyy-MM-ddTHH:mm:ssZ), in UTC (+000). */
    val publishedAt: String = ""
) : Serializable {

    /** The date and time that the article was published (dd.MM.yyyy HH:mm), in local timezone */
    val publishedAtLocal: String
        get() = convertDateToLocal(publishedAt, "dd.MM.yyyy HH:mm")
}