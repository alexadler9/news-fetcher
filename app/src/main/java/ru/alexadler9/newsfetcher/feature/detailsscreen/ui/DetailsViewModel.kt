package ru.alexadler9.newsfetcher.feature.detailsscreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.feature.detailsscreen.ArticleDetailsInteractor

class DetailsViewModel @AssistedInject constructor(
    private val interactor: ArticleDetailsInteractor,
    @Assisted private val article: ArticleModel
) : BaseViewModel<ViewState, ViewEvent>() {

    @AssistedFactory
    interface DetailsViewModelFactory {
        fun create(article: ArticleModel): DetailsViewModel
    }

    override val initialViewState = ViewState(
        article = article,
        state = State.Load
    )

    init {
        wallpaperLoad(article)
    }

    override fun reduce(action: Action, previousState: ViewState): ViewState? {
        return when (action) {
            is DataAction.OnWallpaperLoadSucceed -> {
                return previousState.copy(state = State.Content(wallpaper = action.wallpaper))
            }

            is DataAction.OnWallpaperLoadFailed -> {
                return previousState.copy(state = State.Error(throwable = action.error))
            }

            else -> null
        }
    }

    private fun wallpaperLoad(article: ArticleModel) {
        viewModelScope.launch {
            interactor.getArticleWallpaper(article).fold(
                onError = {
                    processDataAction(DataAction.OnWallpaperLoadFailed(error = it))
                },
                onSuccess = {
                    processDataAction(DataAction.OnWallpaperLoadSucceed(wallpaper = it))
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