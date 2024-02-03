package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.bookmarksscreen.BookmarksInteractor
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(private val interactor: BookmarksInteractor) :
    BaseViewModel<ViewState, ViewEvent>() {

    override val initialViewState = ViewState(
        state = State.Load
    )

    private val bookmarksFlow: StateFlow<List<ArticleItem>> =
        interactor.getArticleBookmarks()
            .map {
                it.reversed().map { article ->
                    ArticleItem(data = article, bookmarked = true)
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            bookmarksFlow.collectLatest {
                processDataAction(DataAction.OnBookmarksLoaded(bookmarks = it))
            }
        }
    }

    override fun reduce(action: Action, previousState: ViewState): ViewState? {
        return when (action) {
            is UiAction.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    bookmarkDelete(action.article)
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

            is DataAction.OnBookmarksLoaded -> {
                previousState.copy(
                    state = State.Content(bookmarks = action.bookmarks)
                )
            }

            else -> null
        }
    }

    private fun bookmarkDelete(article: ArticleItem) = viewModelScope.launch {
        interactor.deleteArticleFromBookmarks(article.data)
    }
}
