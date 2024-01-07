package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.alexadler9.newsfetcher.databinding.FragmentBookmarksBinding
import ru.alexadler9.newsfetcher.feature.adapter.ArticlesAdapter

/**
 * Fragment is responsible for loading, displaying and managing article bookmarks.
 */
@AndroidEntryPoint
class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarksViewModel by viewModels()

    private val bookmarksAdapter: ArticlesAdapter by lazy {
        ArticlesAdapter(onIconBookmarkClicked = {
            viewModel.processUiEvent(UiEvent.OnBookmarkButtonClicked(it))
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

        binding.rvBookmarks.adapter = bookmarksAdapter

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
                    rvBookmarks.isVisible = false
                }

                is State.Content -> {
                    rvBookmarks.isVisible = true
                    bookmarksAdapter.data = viewState.state.bookmarkedArticles
                }

                is State.Error -> {
                    rvBookmarks.isVisible = false
                    Log.d("BOOKMARKS", "Error: ${viewState.state.throwable.message}")
                }
            }
        }
    }
}