package ru.alexadler9.newsfetcher.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.domain.NewsInteractor

class ArticlesScreenViewModel(private val interactor: NewsInteractor) :
    BaseViewModel<ViewState>() {

    override fun initialViewState(): ViewState {
        articlesLoad()
        return ViewState(
            state = State.Load
        )
    }

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        return when (event) {
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