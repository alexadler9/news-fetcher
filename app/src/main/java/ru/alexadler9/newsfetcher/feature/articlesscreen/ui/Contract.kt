package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import androidx.paging.PagingData
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem

sealed class State {
    object Load : State()
    data class Content(val articlesPagingData: PagingData<ArticleItem>) : State()
    data class Error(val throwable: Throwable) : State()
}

data class ViewState(
    val state: State
)

sealed class ViewEvent {
}

sealed class UiAction : Action {
    data class OnPagerLoadFailed(val error: Throwable, val itemCount: Int) : UiAction()
    data class OnBookmarkButtonClicked(val article: ArticleItem) : UiAction()
}

sealed class DataAction : Action {
    data class OnArticlesLoadSucceed(val articlesPagingData: PagingData<ArticleItem>) : DataAction()
    data class OnBookmarksUpdated(val bookmarksUrls: Set<String>) : DataAction()
}
