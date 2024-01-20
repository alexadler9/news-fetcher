package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem

sealed class State {
    object Load : State()
    data class Content(val articles: List<ArticleItem>) : State()
    data class Error(val throwable: Throwable) : State()
}

data class ViewState(
    val state: State
)

sealed class ViewEvent {
}

sealed class UiAction : Action {
    data class OnBookmarkButtonClicked(val index: Int) : UiAction()
}

sealed class DataAction : Action {
    data class OnArticlesLoadSucceed(val articles: List<ArticleItem>) : DataAction()
    data class OnArticlesLoadFailed(val error: Throwable) : DataAction()
    data class OnBookmarksUpdated(val bookmarks: List<ArticleModel>) : DataAction()
}
