package ru.alexadler9.newsfetcher.domain.model

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

    /** The date and time that the article was published (dd.MM.yyyy HH:mm). */
    val publishedAt: String = ""
) : Serializable