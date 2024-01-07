package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.alexadler9.newsfetcher.databinding.FragmentArticlesBinding
import ru.alexadler9.newsfetcher.feature.adapter.ArticlesAdapter

/**
 * Fragment is responsible for loading, displaying and managing the list of article headlines.
 */
@AndroidEntryPoint
class ArticlesFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticlesViewModel by viewModels()

    private val articlesAdapter: ArticlesAdapter by lazy {
        ArticlesAdapter(onIconBookmarkClicked = {
            viewModel.processUiEvent(UiEvent.OnBookmarkButtonClicked(it))
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvArticles.adapter = articlesAdapter

        viewModel.viewState.observe(viewLifecycleOwner, ::render)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(viewState: ViewState) {
        with(binding) {
            when (viewState.state) {
                is State.Load -> {
                    rvArticles.isVisible = false
                }

                is State.Content -> {
                    rvArticles.isVisible = true
                    articlesAdapter.submitList(viewState.state.articles)
                }

                is State.Error -> {
                    rvArticles.isVisible = false
                    Log.d("ARTICLES", "Error: ${viewState.state.throwable.message}")
                }
            }
        }
    }
}