package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem

sealed class State {
    object Load : State()
    data class Content(val bookmarks: List<ArticleItem>) : State()
}

data class ViewState(
    val state: State
)

sealed class UiEvent : Event {
    data class OnBookmarkButtonClicked(val index: Int) : UiEvent()
}

sealed class DataEvent : Event {
    data class OnBookmarksLoaded(val bookmarks: List<ArticleItem>) : DataEvent()
}