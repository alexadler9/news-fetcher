package ru.alexadler9.newsfetcher.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.databinding.ItemArticleBinding

class ArticlesAdapter2(
    val onItemClicked: (ArticleItem) -> Unit = {},
    val onIconShareClicked: (ArticleItem) -> Unit = {},
    val onIconBookmarkClicked: (ArticleItem) -> Unit = {}
) : PagingDataAdapter<ArticleItem, ArticlesAdapter2.ArticleViewHolder>(ArticlesDiffItemCallback()) {

    class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ArticleItem?,
            onItemClicked: (ArticleItem) -> Unit = {},
            onIconShareClicked: (ArticleItem) -> Unit = {},
            onIconBookmarkClicked: (ArticleItem) -> Unit = {}
        ) {
            with(binding) {
//                itemView.setOnClickListener {
//                    onItemClicked(item)
//                }
//                ivShare.apply {
//                    setOnClickListener {
//                        onIconShareClicked(item)
//                    }
//                    isEnabled = (onIconShareClicked != {})
//                }
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
                    isEnabled = (onIconBookmarkClicked != {})
                }
//                ivBrowser.apply {
//                    setOnClickListener {
//                        articleLinkOpen(context, item)
//                    }
//                }
                tvAuthor.text = item?.data?.author?.ifBlank { "[Undefined]" } ?: "ololo"
                tvDate.text = item?.data?.publishedAtLocal ?: "ololo"
                tvTitle.text = item?.data?.title ?: "ololo"
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