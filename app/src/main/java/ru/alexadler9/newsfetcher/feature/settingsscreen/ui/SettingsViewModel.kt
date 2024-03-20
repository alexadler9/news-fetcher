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
        category = interactor.getArticlesCategory(),
        isPolling = interactor.newsPollEnabled()
    )

    override fun reduce(action: Action, previousState: ViewState): ViewState? {
        return when (action) {
            is UiAction.OnCountryChanged -> {
                if (action.country != previousState.country) {
                    interactor.saveArticlesCountry(action.country)
                    return previousState.copy(country = action.country)
                }
                null
            }

            is UiAction.OnCategoryChanged -> {
                if (action.category != previousState.category) {
                    interactor.saveArticlesCategory(action.category)
                    return previousState.copy(category = action.category)
                }
                null
            }

            is UiAction.OnNewsPollingChanged -> {
                interactor.saveNewsPoll(action.isOn)
                previousState.copy(isPolling = action.isOn)
            }

            else -> null
        }
    }
}