package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.model.ArticleModel

sealed class State {
    object Load : State()
    data class Content(val articles: List<ArticleModel>) : State()
    data class Error(val throwable: Throwable) : State()
}

data class ViewState(
    val state: State
)

sealed class UiEvent : Event {
    data class OnBookmarkButtonClicked(val index: Int) : UiEvent()
}

sealed class DataEvent : Event {
    data class OnArticlesLoadSucceed(val articles: List<ArticleModel>) : DataEvent()
    data class OnArticlesLoadFailed(val error: Throwable) : DataEvent()
}
