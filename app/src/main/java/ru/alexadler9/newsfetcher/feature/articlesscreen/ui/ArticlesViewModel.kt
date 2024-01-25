package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.articlesscreen.ArticlesInteractor
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val interactor: ArticlesInteractor) :
    BaseViewModel<ViewState, ViewEvent>() {

    override val initialViewState = ViewState(
        articlesPagingData = PagingData.empty(),
        state = State.Load
    )

    private val countryFlow = MutableStateFlow(interactor.getArticlesCountry())

    private val bookmarksUrlsFlow: StateFlow<Set<String>> =
        interactor.getArticleBookmarks()
            .map { bookmarks ->
                bookmarks.map { bookmark ->
                    bookmark.url
                }.toHashSet()
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val articlesPagingDataFlow: StateFlow<PagingData<ArticleItem>> =
        countryFlow.map(::newArticlesPager)
            .flatMapLatest { pager ->
                pager.flow
            }
            .map { pagingData ->
                pagingData.map { articleModel ->
                    ArticleItem(articleModel, false)
                }
            }
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    init {
        viewModelScope.launch {
            launch {
                bookmarksUrlsFlow.collectLatest {
                    processDataAction(DataAction.OnBookmarksUpdated(bookmarksUrls = it))
                }
            }
            launch {
                articlesPagingDataFlow.collectLatest {
                    processDataAction(DataAction.OnArticlesLoadSucceed(articlesPagingData = it))
                }
            }
        }
    }

    override fun reduce(action: Action, previousState: ViewState): ViewState? {
        return when (action) {
            is UiAction.OnApplySettings -> {
                countryFlow.compareAndSet(countryFlow.value, interactor.getArticlesCountry())
                null
            }

            is UiAction.OnPagerStateChanged -> {
                if (previousState.state is State.Load) {
                    val refreshState = action.state.refresh
                    if (refreshState is LoadState.Error) {
                        return previousState.copy(state = State.Error(refreshState.error))
                    }
                    if (refreshState is LoadState.NotLoading) {
                        return previousState.copy(state = State.Content)
                    }
                    return null
                }
                if (previousState.state is State.Content) {
                    val appendState = action.state.append
                    if ((appendState is LoadState.Error) || (appendState is LoadState.NotLoading)) {
                        // Pager itself will display display an error or loading.
                        return null
                    }
                }
                null
            }

            is UiAction.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    changeArticleBookmark(action.article)
                }
                null
            }

            is DataAction.OnArticlesLoadSucceed -> {
                val articlesPagingData = action.articlesPagingData.map {
                    bookmarkArticle(it, bookmarksUrlsFlow.value)
                }
                previousState.copy(articlesPagingData = articlesPagingData)
            }

            is DataAction.OnBookmarksUpdated -> {
                val articlesPagingData = previousState.articlesPagingData.map {
                    bookmarkArticle(it, action.bookmarksUrls)
                }
                return previousState.copy(articlesPagingData = articlesPagingData)
            }

            else -> null
        }
    }

    private fun newArticlesPager(country: ArticlesCountry): Pager<Int, ArticleModel> =
        Pager(PagingConfig(5, enablePlaceholders = false)) {
            interactor.getTopHeadlinesArticlesPagingSource(country)
        }

    private fun bookmarkArticle(article: ArticleItem, bookmarksUrl: Set<String>) =
        article.copy(bookmarked = bookmarksUrl.contains(article.data.url))

    private fun changeArticleBookmark(article: ArticleItem) = viewModelScope.launch {
        interactor.changeArticleBookmark(article.data)
    }
}