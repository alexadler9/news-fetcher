package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

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
import ru.alexadler9.newsfetcher.BuildConfig
import ru.alexadler9.newsfetcher.base.ext.setAdapterAndCleanupOnDetachFromWindow
import ru.alexadler9.newsfetcher.databinding.FragmentBookmarksBinding
import ru.alexadler9.newsfetcher.feature.adapter.ArticlesListAdapter

/**
 * Fragment is responsible for loading, displaying and managing article bookmarks.
 * Use the [BookmarksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarksViewModel by viewModels()

    private val bookmarksAdapter: ArticlesListAdapter by lazy {
        ArticlesListAdapter(
            onItemClicked = { article ->
                if (BuildConfig.FLAVOR != "demo") {
                    BookmarksFragmentDirections.actionBookmarksFragmentToDetailsFragment(article.data)
                        .apply {
                            findNavController().navigate(this)
                        }
                }
            },
            onIconShareClicked = { article ->
                viewModel.processUiAction(
                    UiAction.OnShareButtonClicked(
                        this@BookmarksFragment.requireContext(),
                        article
                    )
                )
            },
            onIconBrowserClicked = { article ->
                viewModel.processUiAction(
                    UiAction.OnBrowserButtonClicked(
                        this@BookmarksFragment.requireContext(),
                        article
                    )
                )
            },
            onIconBookmarkClicked = { article ->
                viewModel.processUiAction(UiAction.OnBookmarkButtonClicked(article))
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvBookmarks.apply {
            adapter = bookmarksAdapter
            setAdapterAndCleanupOnDetachFromWindow(bookmarksAdapter)
        }

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
                    pbBookmarks.isVisible = true
                    layoutEmpty.isVisible = false
                    rvBookmarks.isVisible = false
                }

                is State.Content -> {
                    pbBookmarks.isVisible = false
                    layoutEmpty.isVisible = viewState.state.bookmarks.isEmpty()
                    rvBookmarks.isVisible = true
                    bookmarksAdapter.submitList(viewState.state.bookmarks)
                }
            }
        }
    }

    companion object {
        /**
         * Create a new instance of this fragment.
         *
         * @return A new instance of fragment BookmarksFragment.
         */
        fun newInstance() = BookmarksFragment()
    }
}