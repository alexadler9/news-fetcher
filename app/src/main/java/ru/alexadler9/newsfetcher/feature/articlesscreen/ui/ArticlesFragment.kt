package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
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
        // Notify the ViewModel about current state. It will decide what to do next.
        viewModel.processUiAction(
            UiAction.OnPagerStateChanged(state = state.source)
        )
    }

    private fun render(viewState: ViewState) {
        viewLifecycleOwner.lifecycleScope.launch {
            articlesAdapter.submitData(viewState.articlesPagingData)
        }
        with(binding) {
            when (viewState.state) {
                is State.Load -> {
                    pbArticles.isVisible = true
                    layoutError.isVisible = false
                    rvArticles.isVisible = false
                }

                is State.Content -> {
                    pbArticles.isVisible = false
                    layoutError.isVisible = false
                    rvArticles.isVisible = true
                }

                is State.Error -> {
                    pbArticles.isVisible = false
                    layoutError.isVisible = true
                    rvArticles.isVisible = false
                    tvError.text = viewState.state.throwable.localizedMessage
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