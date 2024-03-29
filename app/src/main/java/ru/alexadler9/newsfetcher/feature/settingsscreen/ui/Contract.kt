package ru.alexadler9.newsfetcher.feature.settingsscreen.ui

import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry

data class ViewState(
    val country: ArticlesCountry,
    val category: ArticlesCategory,
    val isPolling: Boolean
)

sealed class ViewEvent {
    object OnStartNewsPolling : ViewEvent()
    object OnStopNewsPolling : ViewEvent()
}

sealed class UiAction : Action {
    data class OnCountryChanged(val country: ArticlesCountry) : UiAction()
    data class OnCategoryChanged(val category: ArticlesCategory) : UiAction()
    data class OnNewsPollingChanged(val isOn: Boolean) : UiAction()
}

sealed class DataAction : Action {
}