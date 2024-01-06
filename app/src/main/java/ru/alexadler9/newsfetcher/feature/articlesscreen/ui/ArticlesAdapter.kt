package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.model.ArticleModel

class ArticlesAdapter(
    val onIconBookmarkClicked: (Int) -> Unit
) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    var data = listOf<ArticleModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ArticleViewHolder(val rootView: CardView) : RecyclerView.ViewHolder(rootView) {

        val tvAuthor = rootView.findViewById<TextView>(R.id.tvAuthor)
        val tvDate = rootView.findViewById<TextView>(R.id.tvDate)
        val tvTitle = rootView.findViewById<TextView>(R.id.tvTitle)
        val ivBookmark = rootView.findViewById<ImageView>(R.id.ivBookmark)

        fun bind(item: ArticleModel, position: Int, onIconBookmarkClicked: (Int) -> Unit) {
            ivBookmark.setOnClickListener {
                onIconBookmarkClicked(position)
            }
            tvAuthor.text = item.author.ifBlank { "[Undefined]" }
            tvDate.text = item.publishedAt
            tvTitle.text = item.title
        }

        companion object {
            fun inflateFrom(parent: ViewGroup): ArticleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_article, parent, false) as CardView
                return ArticleViewHolder(view)
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