package ru.alexadler9.newsfetcher.data.remote.model

import com.google.gson.annotations.SerializedName

data class ArticlesRemoteModel(
    @SerializedName("articles")
    val articleList: List<ArticleRemoteModel>
)
