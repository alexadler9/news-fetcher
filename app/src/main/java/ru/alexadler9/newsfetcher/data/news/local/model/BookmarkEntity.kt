package ru.alexadler9.newsfetcher.data.news.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val BOOKMARKS_TABLE = "bookmarks"

@Entity(tableName = BOOKMARKS_TABLE)
data class BookmarkEntity(
    /** The direct URL to the article. */
    @PrimaryKey
    @ColumnInfo(name = "url")
    val url: String,

    /** The headline or title of the article. */
    @ColumnInfo(name = "title")
    val title: String,

    /** The author of the article. */
    @ColumnInfo(name = "author")
    val author: String,

    /** A description or snippet from the article. */
    @ColumnInfo(name = "description")
    val description: String,

    /** The URL to a relevant image for the article. */
    @ColumnInfo(name = "urlToImage")
    val urlToImage: String,

    /** The date and time that the article was published (yyyy-MM-ddTHH:mm:ssZ), in UTC (+000). */
    @ColumnInfo(name = "publishedAt")
    val publishedAt: String
)