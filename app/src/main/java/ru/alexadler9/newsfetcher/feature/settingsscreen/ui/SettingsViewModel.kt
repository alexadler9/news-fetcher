package ru.alexadler9.newsfetcher.feature.settingsscreen.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.feature.settingsscreen.SettingsInteractor
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val interactor: SettingsInteractor) :
    BaseViewModel<ViewState, ViewEvent>() {

    override val initialViewState = ViewState(
        country = interactor.getArticlesCountry(),
        category = interactor.getArticlesCategory()
    )

    override fun reduce(action: Action, previousState: ViewState): ViewState? {
        return when (action) {
            else -> null
        }
    }
}