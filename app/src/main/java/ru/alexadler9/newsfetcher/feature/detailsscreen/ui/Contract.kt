package ru.alexadler9.newsfetcher.feature.detailsscreen.ui

import android.content.Context
import android.graphics.Bitmap
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.domain.model.ArticleModel

sealed class State {
    object Load : State()
    data class Content(val wallpaper: Bitmap) : State()
    data class Error(val throwable: Throwable) : State()
}

data class ViewState(
    val article: ArticleModel,
    val state: State
)

sealed class ViewEvent {
}

sealed class UiAction : Action {
    data class OnShareMenuClicked(val context: Context) : UiAction()
    data class OnBrowserMenuClicked(val context: Context) : UiAction()
}

sealed class DataAction : Action {
    data class OnWallpaperLoadSucceed(val wallpaper: Bitmap) : DataAction()
    data class OnWallpaperLoadFailed(val error: Throwable) : DataAction()
}