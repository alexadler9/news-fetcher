package ru.alexadler9.newsfetcher.feature.adapter

import androidx.recyclerview.widget.DiffUtil

class ArticlesDiffItemCallback : DiffUtil.ItemCallback<ArticleItem>() {

    override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
        return (oldItem.data.url == newItem.data.url)
    }

    override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
        return (oldItem == newItem)
    }
}