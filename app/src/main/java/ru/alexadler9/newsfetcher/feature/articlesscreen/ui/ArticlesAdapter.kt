package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alexadler9.newsfetcher.databinding.ItemArticleBinding
import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.model.ArticleModel

class ArticlesAdapter(
    val onIconBookmarkClicked: (Int) -> Unit
) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    var data = listOf<ArticleModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ArticleViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ArticleModel, position: Int, onIconBookmarkClicked: (Int) -> Unit) {
            with(binding) {
                ivBookmark.setOnClickListener {
                    onIconBookmarkClicked(position)
                }
                tvAuthor.text = item.author.ifBlank { "[Undefined]" }
                tvDate.text = item.publishedAt
                tvTitle.text = item.title
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
        val item = data[position]
        holder.bind(item, position, onIconBookmarkClicked)
    }

    override fun getItemCount() = data.size
}