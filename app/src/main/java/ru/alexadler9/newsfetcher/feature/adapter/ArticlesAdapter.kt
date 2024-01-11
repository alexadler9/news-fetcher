package ru.alexadler9.newsfetcher.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.databinding.ItemArticleBinding
import ru.alexadler9.newsfetcher.domain.model.ArticleModel

class ArticlesAdapter(
    val onItemClicked: (ArticleModel) -> Unit = {},
    val onIconSendClicked: (ArticleModel) -> Unit = {},
    val onIconBookmarkClicked: (Int) -> Unit = {}
) : ListAdapter<ArticleItem, ArticlesAdapter.ArticleViewHolder>(ArticlesDiffItemCallback()) {

    class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ArticleItem,
            onItemClicked: (ArticleModel) -> Unit = {},
            onIconSendClicked: (ArticleModel) -> Unit = {},
            onIconBookmarkClicked: (Int) -> Unit = {}
        ) {
            with(binding) {
                itemView.setOnClickListener {
                    onItemClicked(item.data)
                }
                ivSend.apply {
                    setOnClickListener {
                        onIconSendClicked(item.data)
                    }
                    isEnabled = (onIconSendClicked != {})
                }
                ivBookmark.apply {
                    setImageResource(
                        if (item.bookmarked)
                            R.drawable.ic_baseline_bookmark_24
                        else
                            R.drawable.ic_baseline_bookmark_border_24
                    )
                    setOnClickListener {
                        onIconBookmarkClicked(adapterPosition)
                    }
                    isEnabled = (onIconBookmarkClicked != {})
                }
                tvAuthor.text = item.data.author.ifBlank { "[Undefined]" }
                tvDate.text = item.data.publishedAt
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
        holder.bind(item, onItemClicked, onIconSendClicked, onIconBookmarkClicked)
    }
}