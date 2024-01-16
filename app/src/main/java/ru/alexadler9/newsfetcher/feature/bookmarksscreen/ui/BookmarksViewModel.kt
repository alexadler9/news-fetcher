package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.bookmarksscreen.BookmarksInteractor
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(private val interactor: BookmarksInteractor) :
    BaseViewModel<ViewState>() {

    private var init: Boolean = true
    private var bookmarkedArticles: List<ArticleItem> = emptyList()

    override fun initialViewState(): ViewState {
        return ViewState(
            state = State.Load
        )
    }

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        return when (event) {
            is UiEvent.OnViewCreated -> {
                if (init) {
                    init = false
                    bookmarksLoad()
                    return previousState.copy(state = State.Load)
                }
                null
            }

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
                    processDataEvent(DataEvent.OnBookmarksLoadFailed(error = it))
                },
                onSuccess = { articles ->
                    processDataEvent(DataEvent.OnBookmarksLoadSucceed(
                        bookmarkedArticles = articles.reversed().map { article ->
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