package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.alexadler9.newsfetcher.R

@AndroidEntryPoint
class ArticlesFragment : Fragment() {

    private val articlesAdapter: ArticlesAdapter by lazy {
        ArticlesAdapter()
    }

    private val rvArticles by lazy {
        requireActivity().findViewById<RecyclerView>(R.id.rvArticles)
            .apply { adapter = articlesAdapter }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity()).get(ArticlesViewModel::class.java)
        viewModel.viewState.observe(viewLifecycleOwner, ::render)
    }

    private fun render(viewState: ViewState) {
        when (viewState.state) {
            is State.Load -> {
                rvArticles.isVisible = false
            }

            is State.Content -> {
                rvArticles.isVisible = true
                articlesAdapter.data = viewState.state.articles
            }

            is State.Error -> {
                rvArticles.isVisible = false
                Log.d("ARTICLES", "Error: ${viewState.state.throwable.message}")
            }
        }
    }
}