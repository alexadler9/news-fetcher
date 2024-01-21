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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.databinding.FragmentArticlesBinding
import ru.alexadler9.newsfetcher.feature.adapter.ArticlesAdapter2
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

    private val articlesAdapter: ArticlesAdapter2 by lazy {
        ArticlesAdapter2(
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

        binding.rvArticles.adapter = articlesAdapter

        viewModel.viewState
            .onEach(::render)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(viewState: ViewState) {
        with(binding) {
            when (viewState.state) {
                is State.Load -> {
                    // rvArticles.isVisible = false
                }

                is State.Content -> {
                    rvArticles.isVisible = true
                    viewLifecycleOwner.lifecycleScope.launch {
                        articlesAdapter.submitData(viewState.state.articlesPagingData)
                    }
                }

                is State.Error -> {
                    //  rvArticles.isVisible = false
                    //  Log.d("ARTICLES", "Error: ${viewState.state.throwable.message}")
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