package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.articlesscreen.ArticlesInteractor
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val interactor: ArticlesInteractor) :
    BaseViewModel<ViewState, ViewEvent>() {

    override val initialViewState = ViewState(
        state = State.Load
    )

    private val bookmarksUrlsFlow: StateFlow<Set<String>> =
        interactor.getArticleBookmarks()
            .map { bookmarks ->
                bookmarks.map { bookmark ->
                    bookmark.url
                }.toHashSet()
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())

    private val articlesPagingDataFlow: StateFlow<PagingData<ArticleItem>> =
        newArticlesPager()
            .flow
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
            is UiAction.OnPagerLoadFailed -> {
                if (action.itemCount == 0) {
                    // Show error state.
                    // If items > 0, the pager itself will display an error.
                    return previousState.copy(state = State.Error(action.error))
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
                previousState.copy(state = State.Content(articlesPagingData = articlesPagingData))
            }

            is DataAction.OnBookmarksUpdated -> {
                if (previousState.state is State.Content) {
                    val articlesPagingData = previousState.state.articlesPagingData.map {
                        bookmarkArticle(it, action.bookmarksUrls)
                    }
                    return previousState.copy(state = State.Content(articlesPagingData = articlesPagingData))
                }
                null
            }

            else -> null
        }
    }

    private fun newArticlesPager(): Pager<Int, ArticleModel> =
        Pager(PagingConfig(5, enablePlaceholders = false)) {
            interactor.getTopHeadlinesArticlesPagingSource()
        }

    private fun bookmarkArticle(article: ArticleItem, bookmarksUrl: Set<String>) =
        article.copy(bookmarked = bookmarksUrl.contains(article.data.url))

    private fun changeArticleBookmark(article: ArticleItem) = viewModelScope.launch {
        interactor.changeArticleBookmark(article.data)
    }
}