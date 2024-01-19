package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.base.Event
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.articlesscreen.ArticlesInteractor
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val interactor: ArticlesInteractor) :
    BaseViewModel<ViewState>() {

    var bookmarks: List<ArticleModel> = emptyList()

    override fun initialViewState(): ViewState {
        viewModelScope.launch {
            delay(100)
            articlesLoad()
            interactor.getArticleBookmarks()
                .collect {
                    processDataEvent(DataEvent.OnBookmarksUpdated(bookmarks = it))
                }
        }
        return ViewState(
            state = State.Load
        )
    }

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        return when (event) {
            is UiEvent.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    val item = previousState.state.articles[event.index]
                    changeArticleBookmark(item)
                }
                null
            }

            is DataEvent.OnArticlesLoadSucceed -> {
                bookmarkArticles(event.articles, bookmarks)
                previousState.copy(state = State.Content(articles = event.articles))
            }

            is DataEvent.OnArticlesLoadFailed -> {
                previousState.copy(state = State.Error(event.error))
            }

            is DataEvent.OnBookmarksUpdated -> {
                bookmarks = event.bookmarks
                if (previousState.state is State.Content) {
                    val articles = previousState.state.articles.map { it.copy() }
                    bookmarkArticles(articles, bookmarks)
                    return previousState.copy(state = State.Content(articles = articles))
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
                    processDataEvent(DataEvent.OnArticlesLoadFailed(error = it))
                },
                onSuccess = {
                    processDataEvent(
                        DataEvent.OnArticlesLoadSucceed(
                            articles = it.map { article ->
                                ArticleItem(article, false)
                            }
                        )
                    )
                }
            )
        }
    }

    private fun bookmarkArticles(articles: List<ArticleItem>, bookmarks: List<ArticleModel>) {
        val bookmarksSet = bookmarks.map { it.url }.toHashSet()
        articles.forEach {
            it.bookmarked = bookmarksSet.contains(it.data.url)
        }
    }

    private fun changeArticleBookmark(item: ArticleItem) {
        viewModelScope.launch {
            interactor.changeArticleBookmark(item.data)
        }
    }
}