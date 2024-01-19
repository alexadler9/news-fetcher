package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.bookmarksscreen.BookmarksInteractor
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(private val interactor: BookmarksInteractor) :
    BaseViewModel<ViewState>() {

    override fun initialViewState(): ViewState {
        viewModelScope.launch {
            delay(100)
            interactor.getArticleBookmarks()
                .collect {
                    processDataEvent(DataEvent.OnBookmarksLoaded(
                        bookmarks = it.reversed().map { article ->
                            ArticleItem(data = article, bookmarked = true)
                        }
                    ))
                }
        }
        return ViewState(
            state = State.Load
        )
    }

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        return when (event) {
            is UiEvent.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    val item = previousState.state.bookmarks[event.index]
                    bookmarkDelete(item)
                }
                null
            }

            is DataEvent.OnBookmarksLoaded -> {
                previousState.copy(
                    state = State.Content(bookmarks = event.bookmarks)
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