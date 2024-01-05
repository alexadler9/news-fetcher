package ru.alexadler9.newsfetcher.data.remote.model

import com.google.gson.annotations.SerializedName

data class ArticlesRemoteModel(
    /** List of articles data. */
    @SerializedName("articles")
    val articleList: List<ArticleRemoteModel>
)
