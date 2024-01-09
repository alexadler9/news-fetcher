package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.bookmarksscreen.BookmarksInteractor
import javax.inject.Inject

private const val LOG_TAG = "BOOKMARKS"

@HiltViewModel
class BookmarksViewModel @Inject constructor(private val interactor: BookmarksInteractor) :
    BaseViewModel<ViewState>() {

    private var bookmarkedArticles: List<ArticleItem> = emptyList()

    override fun initialViewState(): ViewState {
        bookmarksLoad()
        return ViewState(
            state = State.Load
        )
    }

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        return when (event) {
            is UiEvent.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    val item = previousState.state.bookmarkedArticles[event.index]
                    bookmarkDelete(item, event.index)
                }
                previousState.copy(state = State.Load)
            }

            is DataEvent.OnBookmarksLoadSucceed -> {
                bookmarkedArticles = event.bookmarkedArticles
                previousState.copy(
                    state = State.Content(bookmarkedArticles = bookmarkedArticles)
                )
            }

            is DataEvent.OnBookmarksLoadFailed -> {
                previousState.copy(state = State.Error(throwable = event.error))
            }

            is DataEvent.OnBookmarkDeleteSucceed -> {
                bookmarkedArticles = bookmarkedArticles.filterIndexed { index, _ ->
                    index != event.index
                }
                previousState.copy(
                    state = State.Content(bookmarkedArticles = bookmarkedArticles)
                )
            }

            else -> null
        }
    }

    private fun bookmarksLoad() {
        viewModelScope.launch {
            interactor.getArticleBookmarks().fold(
                onError = {
                    Log.e(LOG_TAG, "Error load bookmarks: ${it.message}")
                    processDataEvent(DataEvent.OnBookmarksLoadFailed(error = it))
                },
                onSuccess = { articles ->
                    processDataEvent(DataEvent.OnBookmarksLoadSucceed(
                        bookmarkedArticles = articles.map { article ->
                            ArticleItem(data = article, bookmarked = true)
                        }
                    ))
                }
            )
        }
    }

    private fun bookmarkDelete(item: ArticleItem, index: Int) {
        viewModelScope.launch {
            interactor.deleteArticleFromBookmarks(item.data)
            processDataEvent(DataEvent.OnBookmarkDeleteSucceed(index))
        }
    }
}