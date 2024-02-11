package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.content.Context
import androidx.paging.LoadStates
import androidx.paging.PagingData
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem

sealed class State {
    object Load : State()
    data class Content(val itemCount: Int) : State()
    data class Error(val throwable: Throwable) : State()
}

data class ViewState(
    val articlesPagingData: PagingData<ArticleItem>,
    val articlesQuery: String,
    val state: State
)

sealed class ViewEvent {
}

sealed class UiAction : Action {
    object OnApplySettings : UiAction()
    data class OnApplyQuery(val query: String) : UiAction()
    data class OnPagerStateChanged(val state: LoadStates, val itemCount: Int) : UiAction()
    data class OnBookmarkButtonClicked(val article: ArticleItem) : UiAction()
    data class OnShareButtonClicked(val context: Context, val article: ArticleItem) : UiAction()
    data class OnBrowserButtonClicked(val context: Context, val article: ArticleItem) : UiAction()
}

sealed class DataAction : Action {
    data class OnArticlesLoadSucceed(val articlesPagingData: PagingData<ArticleItem>) : DataAction()
    data class OnBookmarksUpdated(val bookmarksUrls: Set<String>) : DataAction()
}
