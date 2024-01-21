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

    private var bookmarksUrl: Set<String> = emptySet()

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
                interactor.getArticleBookmarks()
                    .map { bookmarks ->
                        bookmarks.map { bookmark ->
                            bookmark.url
                        }.toHashSet()
                    }
                    .collectLatest {
                        processDataAction(DataAction.OnBookmarksUpdated(bookmarksUrl = it))
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
            is UiAction.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    changeArticleBookmark(action.article.data)
                }
                null
            }

            is DataAction.OnArticlesLoadSucceed -> {
                val articlesPagingData = action.articlesPagingData.map {
                    bookmarkArticle(it, bookmarksUrl)
                }
                previousState.copy(state = State.Content(articlesPagingData = articlesPagingData))
            }

//            is DataAction.OnArticlesLoadFailed -> {
//                previousState.copy(state = State.Error(action.error))
//            }

            is DataAction.OnBookmarksUpdated -> {
                bookmarksUrl = action.bookmarksUrl
                if (previousState.state is State.Content) {
                    val articlesPagingData = previousState.state.articlesPagingData.map {
                        bookmarkArticle(it, bookmarksUrl)
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

    private fun changeArticleBookmark(item: ArticleModel) = viewModelScope.launch {
        interactor.changeArticleBookmark(item)
    }
}