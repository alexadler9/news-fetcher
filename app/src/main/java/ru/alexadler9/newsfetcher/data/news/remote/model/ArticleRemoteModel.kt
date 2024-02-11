package ru.alexadler9.newsfetcher.data.news.remote.model

import com.google.gson.annotations.SerializedName

data class ArticleRemoteModel(
    /** The author of the article. */
    @SerializedName("author")
    val author: String?,

    /** The headline or title of the article. */
    @SerializedName("title")
    val title: String,

    /** A description or snippet from the article. */
    @SerializedName("description")
    val description: String?,

    /** The direct URL to the article. */
    @SerializedName("url")
    val url: String,

    /** The URL to a relevant image for the article. */
    @SerializedName("urlToImage")
    val urlToImage: String?,

    /** The date and time that the article was published (yyyy-MM-ddTHH:mm:ssZ), in UTC (+000). */
    @SerializedName("publishedAt")
    val publishedAt: String
)
