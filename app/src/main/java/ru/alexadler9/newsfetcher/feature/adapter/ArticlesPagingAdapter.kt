package ru.alexadler9.newsfetcher.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.databinding.ItemArticleBinding

class ArticlesPagingAdapter(
    val onItemClicked: (ArticleItem) -> Unit = {},
    val onIconShareClicked: (ArticleItem) -> Unit = {},
    val onIconBrowserClicked: (ArticleItem) -> Unit = {},
    val onIconBookmarkClicked: (ArticleItem) -> Unit = {}
) : PagingDataAdapter<ArticleItem, ArticlesPagingAdapter.ArticleViewHolder>(ArticlesDiffItemCallback()) {

    class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ArticleItem?,
            onItemClicked: (ArticleItem) -> Unit,
            onIconShareClicked: (ArticleItem) -> Unit,
            onIconBrowserClicked: (ArticleItem) -> Unit,
            onIconBookmarkClicked: (ArticleItem) -> Unit
        ) {
            with(binding) {
                itemView.setOnClickListener {
                    item?.let {
                        onItemClicked(item)
                    }
                }
                ivShare.apply {
                    setOnClickListener {
                        onIconShareClicked(item!!)
                    }
                    isEnabled = (item != null)
                }
                ivBookmark.apply {
                    setImageResource(
                        if (item != null && item.bookmarked)
                            R.drawable.ic_baseline_bookmark_24
                        else
                            R.drawable.ic_baseline_bookmark_border_24
                    )
                    setOnClickListener {
                        onIconBookmarkClicked(item!!)
                    }
                    isEnabled = (item != null)
                }
                ivBrowser.apply {
                    setOnClickListener {
                        onIconBrowserClicked(item!!)
                    }
                    isEnabled = (item != null)
                }
                tvAuthor.text = item?.data?.author?.ifBlank { "[Undefined]" } ?: ""
                tvDate.text = item?.data?.publishedAtLocal ?: ""
                tvTitle.text = item?.data?.title ?: ""
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
        holder.bind(
            item,
            onItemClicked,
            onIconShareClicked,
            onIconBrowserClicked,
            onIconBookmarkClicked
        )
    }
}