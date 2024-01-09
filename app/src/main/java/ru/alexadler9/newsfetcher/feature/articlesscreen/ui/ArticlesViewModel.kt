package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.articlesscreen.ArticlesInteractor
import javax.inject.Inject

private const val LOG_TAG = "ARTICLES"

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val interactor: ArticlesInteractor) :
    BaseViewModel<ViewState>() {

    override fun initialViewState(): ViewState {
        articlesLoad()
        return ViewState(
            state = State.Load
        )
    }

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        return when (event) {
            is UiEvent.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    val item = previousState.state.articles[event.index]
                    switchArticleBookmark(item)
                }
                null
            }

            is DataEvent.OnArticlesLoadSucceed -> {
                previousState.copy(state = State.Content(articles = event.articles))
            }

            is DataEvent.OnArticlesLoadFailed -> {
                previousState.copy(state = State.Error(event.error))
            }

            is DataEvent.OnArticleMarkChanged -> {
                if (previousState.state is State.Content) {
                    val articles = previousState.state.articles.map {
                        it.copy(bookmarked = if (it.data == event.article) !it.bookmarked else it.bookmarked)
                    }
                    return previousState.copy(state = State.Content(articles))
                }
                null
            }

            else -> null
        }
    }

    private fun articlesLoad() {
        viewModelScope.launch {
            interactor.getArticles().fold(
                onError = {
                    Log.e(LOG_TAG, "Error load articles: ${it.message}")
                    processDataEvent(DataEvent.OnArticlesLoadFailed(error = it))
                },
                onSuccess = { articles ->
                    val articlesMarked = articles.map { article ->
                        ArticleItem(article, interactor.articleBookmarkExist(article.url).fold(
                            onError = { false },
                            onSuccess = { it }
                        ))
                    }
                    processDataEvent(DataEvent.OnArticlesLoadSucceed(articles = articlesMarked))
                }
            )
        }
    }

    private fun switchArticleBookmark(item: ArticleItem) {
        viewModelScope.launch {
            if (item.bookmarked) {
                interactor.deleteArticleFromBookmarks(item.data)
            } else {
                interactor.addArticleToBookmark(item.data)
            }
            processDataEvent(DataEvent.OnArticleMarkChanged(item.data))
        }
    }
}