package ru.alexadler9.newsfetcher.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.databinding.ItemArticleBinding
import ru.alexadler9.newsfetcher.feature.articleLinkOpen

class ArticlesListAdapter(
    val onItemClicked: (ArticleItem) -> Unit = {},
    val onIconShareClicked: (ArticleItem) -> Unit = {},
    val onIconBookmarkClicked: (ArticleItem) -> Unit = {}
) : ListAdapter<ArticleItem, ArticlesListAdapter.ArticleViewHolder>(ArticlesDiffItemCallback()) {

    class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ArticleItem,
            onItemClicked: (ArticleItem) -> Unit = {},
            onIconShareClicked: (ArticleItem) -> Unit = {},
            onIconBookmarkClicked: (ArticleItem) -> Unit = {}
        ) {
            with(binding) {
                itemView.setOnClickListener {
                    onItemClicked(item)
                }
                ivShare.apply {
                    setOnClickListener {
                        onIconShareClicked(item)
                    }
                }
                ivBookmark.apply {
                    setImageResource(
                        if (item.bookmarked)
                            R.drawable.ic_baseline_bookmark_24
                        else
                            R.drawable.ic_baseline_bookmark_border_24
                    )
                    setOnClickListener {
                        onIconBookmarkClicked(item)
                    }
                }
                ivBrowser.apply {
                    setOnClickListener {
                        articleLinkOpen(context, item.data)
                    }
                }
                tvAuthor.text = item.data.author.ifBlank { "[Undefined]" }
                tvDate.text = item.data.publishedAtLocal
                tvTitle.text = item.data.title
            }
        }

        companion object {

            fun inflateFrom(parent: ViewGroup): ArticleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemArticleBinding.inflate(layoutInflater, parent, false)
                return ArticleViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClicked, onIconShareClicked, onIconBookmarkClicked)
    }
}