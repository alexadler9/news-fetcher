package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alexadler9.newsfetcher.BuildConfig
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.base.ext.toEditable
import ru.alexadler9.newsfetcher.databinding.FragmentArticlesBinding
import ru.alexadler9.newsfetcher.feature.adapter.ArticlesLoaderStateAdapter
import ru.alexadler9.newsfetcher.feature.adapter.ArticlesPagingAdapter

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
                if (BuildConfig.FLAVOR != "demo") {
                    ArticlesFragmentDirections.actionArticlesFragmentToDetailsFragment(article.data)
                        .apply {
                            findNavController().navigate(this)
                        }
                }
            },
            onIconShareClicked = { article ->
                viewModel.processUiAction(
                    UiAction.OnShareButtonClicked(
                        this@ArticlesFragment.requireContext(),
                        article
                    )
                )
            },
            onIconBrowserClicked = { article ->
                viewModel.processUiAction(
                    UiAction.OnBrowserButtonClicked(
                        this@ArticlesFragment.requireContext(),
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
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure recyclerview adapter.

        binding.rvArticles.adapter = articlesAdapter.withLoadStateHeaderAndFooter(
            header = ArticlesLoaderStateAdapter(),
            footer = ArticlesLoaderStateAdapter()
        )

        articlesAdapter.addLoadStateListener(::pagerLoadStateProcess)

        // Add menu items using the MenuProvider API.

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_articles, menu)

                val searchItem: MenuItem = menu.findItem(R.id.menuItemSearch)
                val searchView = searchItem.actionView as SearchView
                searchView.apply {
                    // setOnQueryTextListener not used because it doesn't handle empty requests.
                    // See workaround below.
                    setOnSearchClickListener {
                        // Show last query.
                        searchView.setQuery(viewModel.viewState.value.articlesQuery, false)
                    }
                    queryHint = requireContext().getString(R.string.search_hint)
                }
                // Workaround for handling empty requests.
                val searchEditText =
                    searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
                searchEditText.apply {
                    setOnEditorActionListener { textView, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            textView.text?.let {
                                viewModel.processUiAction(UiAction.OnApplyQuery(it.toString()))
                                searchView.clearFocus()
                            }
                        }
                        false
                    }
                    setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            // Return last query.
                            searchView.setQuery(viewModel.viewState.value.articlesQuery, false)
                        }
                    }
                }
                val closeButton =
                    searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
                closeButton.setOnClickListener {
                    viewModel.processUiAction(UiAction.OnApplyQuery(""))
                    searchEditText.text = "".toEditable()
                    searchView.clearFocus()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return true
            }
        }, viewLifecycleOwner)

        // Configure viewModel.

        viewModel.viewState
            .onEach(::render)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.processUiAction(UiAction.OnApplySettings)
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
        articlesAdapter.submitData(
            viewLifecycleOwner.lifecycle,
            viewState.articlesPagingData
        )
        with(binding) {
            when (viewState.state) {
                is State.Load -> {
                    pbArticles.isVisible = true
                    layoutError.isVisible = false
                    layoutEmpty.isVisible = false
                }

                is State.Content -> {
                    pbArticles.isVisible = false
                    layoutError.isVisible = false
                    layoutEmpty.isVisible = (articlesAdapter.itemCount == 0)
                }

                is State.Error -> {
                    pbArticles.isVisible = false
                    layoutError.isVisible = true
                    tvError.text = viewState.state.throwable.localizedMessage
                    layoutEmpty.isVisible = false
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