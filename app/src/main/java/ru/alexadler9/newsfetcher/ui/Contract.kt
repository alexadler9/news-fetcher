package ru.alexadler9.newsfetcher.ui

import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.domain.model.ArticleModel

sealed class State {
    object Load : State()
    data class Content(val articles: List<ArticleModel>) : State()
    data class Error(val throwable: Throwable) : State()
}

data class ViewState(
    val state: State
)

sealed class UiEvent : Event {
}

sealed class DataEvent : Event {
    data class OnArticlesLoadSucceed(val articles: List<ArticleModel>) : DataEvent()
    data class OnArticlesLoadFailed(val error: Throwable) : DataEvent()
}
