package ru.alexadler9.newsfetcher.feature.detailsscreen.ui

import android.graphics.Bitmap
import ru.alexadler9.newsfetcher.base.Event
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

sealed class UiEvent : Event {
    object OnViewCreated : UiEvent()
}

sealed class DataEvent : Event {
    data class OnWallpaperLoadSucceed(val wallpaper: Bitmap) : DataEvent()
    data class OnWallpaperLoadFailed(val error: Throwable) : DataEvent()
}