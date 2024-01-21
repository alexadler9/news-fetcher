package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.databinding.FragmentArticlesBinding
import ru.alexadler9.newsfetcher.feature.adapter.ArticlesLoaderStateAdapter
import ru.alexadler9.newsfetcher.feature.adapter.ArticlesPagingAdapter
import ru.alexadler9.newsfetcher.feature.articleLinkShare

/**
 * Fragment is responsible for loading, displaying and managing the list of article headlines.
 * Use the [ArticlesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ArticlesFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticlesViewModel by viewModels()

    private val articlesAdapter: ArticlesPagingAdapter by lazy {
        ArticlesPagingAdapter(
            onItemClicked = { article ->
                ArticlesFragmentDirections.actionArticlesFragmentToDetailsFragment(article.data)
                    .apply {
                        findNavController().navigate(this)
                    }
            },
            onIconShareClicked = { article ->
                articleLinkShare(this@ArticlesFragment.requireContext(), article.data)
            },
            onIconBookmarkClicked = {
                viewModel.processUiAction(UiAction.OnBookmarkButtonClicked(it))
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

        binding.rvArticles.adapter = articlesAdapter.withLoadStateHeaderAndFooter(
            header = ArticlesLoaderStateAdapter(),
            footer = ArticlesLoaderStateAdapter()
        )

        articlesAdapter.addLoadStateListener(::pagerLoadStateProcess)

        viewModel.viewState
            .onEach(::render)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pagerLoadStateProcess(state: CombinedLoadStates) {
        val refreshState = state.refresh
        val appendState = state.append

        var error: Throwable? = null
        if (refreshState is LoadState.Error) error = refreshState.error
        if (appendState is LoadState.Error) error = appendState.error

        error?.let {
            // Notify the ViewModel about the error. It will decide what to do next.
            viewModel.processUiAction(
                UiAction.OnPagerLoadFailed(
                    error = error,
                    itemCount = articlesAdapter.itemCount
                )
            )
        }
    }

    private fun render(viewState: ViewState) {
        with(binding) {
            when (viewState.state) {
                is State.Load -> {
                    rvArticles.isVisible = false
                }

                is State.Content -> {
                    rvArticles.isVisible = true
                    viewLifecycleOwner.lifecycleScope.launch {
                        articlesAdapter.submitData(viewState.state.articlesPagingData)
                    }
                }

                is State.Error -> {
                    rvArticles.isVisible = false
                    Log.d("ARTICLES", "Error: ${viewState.state.throwable.message}")
                }
            }
        }
    }

    companion object {
        /**
         * Create a new instance of this fragment.
         *
         * @return A new instance of fragment ArticlesFragment.
         */
        fun newInstance() = ArticlesFragment()
    }
}