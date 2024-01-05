package ru.alexadler9.newsfetcher.domain.model

import java.io.Serializable

data class ArticleModel(
    /** The author of the article. */
    val author: String,

    /** The headline or title of the article. */
    val title: String,

    /** A description or snippet from the article. */
    val description: String,

    /** The date and time that the article was published, in UTC (+000). */
    val publishedAt: String
) : Serializable