package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem

sealed class State {
    object Load : State()
    data class Content(val bookmarkedArticles: List<ArticleItem>) : State()
    data class Error(val throwable: Throwable) : State()
}

data class ViewState(
    val state: State
)

sealed class UiEvent : Event {
    data class OnBookmarkButtonClicked(val index: Int) : UiEvent()
}

sealed class DataEvent : Event {
    data class OnBookmarksLoadSucceed(val bookmarkedArticles: List<ArticleItem>) : DataEvent()
    data class OnBookmarksLoadFailed(val error: Throwable) : DataEvent()
    data class OnBookmarkDeleteSucceed(val index: Int) : DataEvent()
}