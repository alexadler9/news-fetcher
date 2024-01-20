package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        viewModelScope.launch {
            interactor.getArticleBookmarks()
                .collect {
                    processDataAction(DataAction.OnBookmarksLoaded(
                        bookmarks = it.reversed().map { article ->
                            ArticleItem(data = article, bookmarked = true)
                        }
                    ))
                }
        }
    }

    override fun reduce(action: Action, previousState: ViewState): ViewState? {
        return when (action) {
            is UiAction.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    val item = previousState.state.bookmarks[action.index]
                    bookmarkDelete(item)
                }
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

    private fun bookmarkDelete(item: ArticleItem) {
        viewModelScope.launch {
            interactor.deleteArticleFromBookmarks(item.data)
        }
    }
}
