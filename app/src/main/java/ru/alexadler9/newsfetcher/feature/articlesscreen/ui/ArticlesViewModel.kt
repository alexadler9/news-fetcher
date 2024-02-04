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
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.articlesscreen.ArticlesInteractor
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val interactor: ArticlesInteractor) :
    BaseViewModel<ViewState, ViewEvent>() {

    private data class QueryParams(
        val country: ArticlesCountry,
        val category: ArticlesCategory,
        val query: String
    )

    override val initialViewState = ViewState(
        articlesPagingData = PagingData.empty(),
        articlesQuery = "",
        state = State.Load
    )

    private val queryParamsFlow = MutableStateFlow(
        QueryParams(
            interactor.getArticlesCountry(),
            interactor.getArticlesCategory(),
            ""
        )
    )

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
        queryParamsFlow
            .map(::newArticlesPager)
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
                val queryParams = QueryParams(
                    interactor.getArticlesCountry(),
                    interactor.getArticlesCategory(),
                    previousState.articlesQuery
                )
                if (queryParamsFlow.value != queryParams) {
                    queryParamsFlow.value = queryParams
                    return previousState.copy(
                        articlesPagingData = PagingData.empty(),
                        state = State.Load
                    )
                }
                null
            }

            is UiAction.OnApplyQuery -> {
                val queryParams = QueryParams(
                    interactor.getArticlesCountry(),
                    interactor.getArticlesCategory(),
                    action.query
                )
                if (queryParamsFlow.value != queryParams) {
                    queryParamsFlow.value = queryParams
                    return previousState.copy(
                        articlesPagingData = PagingData.empty(),
                        articlesQuery = action.query,
                        state = State.Load
                    )
                }
                previousState.copy(articlesQuery = action.query)
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

            is UiAction.OnShareButtonClicked -> {
                interactor.shareArticle(action.context, action.article.data)
                null
            }

            is UiAction.OnBrowserButtonClicked -> {
                interactor.openArticleInBrowser(action.context, action.article.data)
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

    private fun newArticlesPager(params: QueryParams): Pager<Int, ArticleModel> =
        Pager(PagingConfig(5, enablePlaceholders = false)) {
            interactor.getTopHeadlinesArticlesPagingSource(
                params.country,
                params.category,
                params.query
            )
        }

    private fun bookmarkArticle(article: ArticleItem, bookmarksUrl: Set<String>) =
        article.copy(bookmarked = bookmarksUrl.contains(article.data.url))

    private fun changeArticleBookmark(article: ArticleItem) = viewModelScope.launch {
        interactor.changeArticleBookmark(article.data)
    }
}