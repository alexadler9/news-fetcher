package ru.alexadler9.newsfetcher.feature.detailsscreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.feature.detailsscreen.ArticleDetailsInteractor

class DetailsViewModel @AssistedInject constructor(
    private val interactor: ArticleDetailsInteractor,
    @Assisted private val article: ArticleModel
) : BaseViewModel<ViewState>() {

    @AssistedFactory
    interface DetailsViewModelFactory {
        fun create(article: ArticleModel): DetailsViewModel
    }

    private var init: Boolean = true

    override fun initialViewState(): ViewState {
        return ViewState(
            article = article,
            state = State.Load
        )
    }

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        return when (event) {
            is UiEvent.OnViewCreated -> {
                if (init) {
                    init = false
                    wallpaperLoad(previousState.article)
                    return previousState.copy(state = State.Load)
                }
                null
            }

            is DataEvent.OnWallpaperLoadSucceed -> {
                return previousState.copy(state = State.Content(wallpaper = event.wallpaper))
            }

            is DataEvent.OnWallpaperLoadFailed -> {
                return previousState.copy(state = State.Error(throwable = event.error))
            }

            else -> null
        }
    }

    private fun wallpaperLoad(article: ArticleModel) {
        viewModelScope.launch {
            interactor.getArticleWallpaper(article).fold(
                onError = {
                    processDataEvent(DataEvent.OnWallpaperLoadFailed(error = it))
                },
                onSuccess = {
                    processDataEvent(DataEvent.OnWallpaperLoadSucceed(wallpaper = it))
                }
            )
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: DetailsViewModelFactory,
            article: ArticleModel
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(article) as T
            }
        }
    }
}