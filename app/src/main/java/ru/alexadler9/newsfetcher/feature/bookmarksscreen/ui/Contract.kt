package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import android.content.Context
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem

sealed class State {
    object Load : State()
    data class Content(val bookmarks: List<ArticleItem>) : State()
}

data class ViewState(
    val state: State
)

sealed class ViewEvent {
}

sealed class UiAction : Action {
    data class OnBookmarkButtonClicked(val article: ArticleItem) : UiAction()
    data class OnShareButtonClicked(val context: Context, val article: ArticleItem) : UiAction()
    data class OnBrowserButtonClicked(val context: Context, val article: ArticleItem) : UiAction()
}

sealed class DataAction : Action {
    data class OnBookmarksLoaded(val bookmarks: List<ArticleItem>) : DataAction()
}

