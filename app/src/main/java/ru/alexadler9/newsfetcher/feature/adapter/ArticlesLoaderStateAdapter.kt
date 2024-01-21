package ru.alexadler9.newsfetcher.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.alexadler9.newsfetcher.databinding.ItemErrorBinding
import ru.alexadler9.newsfetcher.databinding.ItemProgressBinding

class ArticlesLoaderStateAdapter() : LoadStateAdapter<ArticlesLoaderStateAdapter.ItemViewHolder>() {

    class ProgressViewHolder(private val binding: ItemProgressBinding) :
        ItemViewHolder(binding.root) {

        override fun bind(loadState: LoadState) {
            // Do nothing
        }

        companion object {

            operator fun invoke(
                layoutInflater: LayoutInflater,
                parent: ViewGroup? = null,
                attachToRoot: Boolean = false
            ): ProgressViewHolder {
                val binding = ItemProgressBinding.inflate(layoutInflater, parent, attachToRoot)
                return ProgressViewHolder(binding)
            }
        }
    }

    class ErrorViewHolder(private val binding: ItemErrorBinding) : ItemViewHolder(binding.root) {

        override fun bind(loadState: LoadState) {
            require(loadState is LoadState.Error)
            binding.tvError.text = loadState.error.localizedMessage
        }

        companion object {

            operator fun invoke(
                layoutInflater: LayoutInflater,
                parent: ViewGroup? = null,
                attachToRoot: Boolean = false
            ): ErrorViewHolder {
                val binding = ItemErrorBinding.inflate(layoutInflater, parent, attachToRoot)
                return ErrorViewHolder(binding)
            }
        }
    }

    abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(loadState: LoadState)
    }

    override fun getStateViewType(loadState: LoadState) = when (loadState) {
        is LoadState.NotLoading -> error("Not supported")
        is LoadState.Loading -> PROGRESS
        is LoadState.Error -> ERROR
    }

    override fun onBindViewHolder(holder: ItemViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemViewHolder {
        return when (loadState) {
            LoadState.Loading -> ProgressViewHolder(LayoutInflater.from(parent.context), parent)
            is LoadState.Error -> ErrorViewHolder(LayoutInflater.from(parent.context), parent)
            is LoadState.NotLoading -> error("Not supported")
        }
    }

    private companion object {

        private const val ERROR = 1
        private const val PROGRESS = 0
    }
}