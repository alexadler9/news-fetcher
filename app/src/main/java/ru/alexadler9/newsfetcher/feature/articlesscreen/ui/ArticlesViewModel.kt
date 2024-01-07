package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.ArticlesInteractor
import javax.inject.Inject

private const val LOG_TAG = "ARTICLES"

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val interactor: ArticlesInteractor) :
    BaseViewModel<ViewState>() {

    override fun initialViewState(): ViewState {
        articlesLoad()
        return ViewState(
            state = State.Load
        )
    }

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        return when (event) {
            is UiEvent.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    Log.d(LOG_TAG, "Bookmark adding...")
                    viewModelScope.launch {
                        val item = previousState.state.articles[event.index]
                        interactor.addArticleToBookmark(item).fold(
                            onError = {
                                Log.d(LOG_TAG, "Error adding a bookmark: ${it.message}")
                            },
                            onSuccess = {
                                Log.d(LOG_TAG, "Bookmark successfully added")
                                interactor.getArticleBookmarks().fold(
                                    onError = {
                                        Log.d(LOG_TAG, "Error get a bookmarks: ${it.message}")
                                    },
                                    onSuccess = {
                                        Log.d(LOG_TAG, "Bookmarks: $it")
                                    }
                                )
                            }
                        )
                    }
                }
                return null
            }

            is DataEvent.OnArticlesLoadSucceed -> {
                previousState.copy(state = State.Content(articles = event.articles))
            }

            is DataEvent.OnArticlesLoadFailed -> {
                previousState.copy(state = State.Error(event.error))
            }

            else -> null
        }
    }

    private fun articlesLoad() {
        viewModelScope.launch {
            interactor.getArticles().fold(
                onError = {
                    processDataEvent(DataEvent.OnArticlesLoadFailed(error = it))
                },
                onSuccess = {
                    processDataEvent(DataEvent.OnArticlesLoadSucceed(articles = it))
                }
            )
        }
    }
}