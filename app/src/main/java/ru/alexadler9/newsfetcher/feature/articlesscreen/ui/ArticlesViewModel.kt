package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.base.Action
import ru.alexadler9.newsfetcher.base.BaseViewModel
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.articlesscreen.ArticlesInteractor
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(private val interactor: ArticlesInteractor) :
    BaseViewModel<ViewState, ViewEvent>() {

    override val initialViewState = ViewState(
        state = State.Load
    )

    var bookmarks: List<ArticleModel> = emptyList()

    init {
        viewModelScope.launch {
            articlesLoad()
            interactor.getArticleBookmarks()
                .collect {
                    processDataAction(DataAction.OnBookmarksUpdated(bookmarks = it))
                }
        }
    }

    override fun reduce(action: Action, previousState: ViewState): ViewState? {
        return when (action) {
            is UiAction.OnBookmarkButtonClicked -> {
                if (previousState.state is State.Content) {
                    val item = previousState.state.articles[action.index]
                    changeArticleBookmark(item)
                }
                null
            }

            is DataAction.OnArticlesLoadSucceed -> {
                bookmarkArticles(action.articles, bookmarks)
                previousState.copy(state = State.Content(articles = action.articles))
            }

            is DataAction.OnArticlesLoadFailed -> {
                previousState.copy(state = State.Error(action.error))
            }

            is DataAction.OnBookmarksUpdated -> {
                bookmarks = action.bookmarks
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
                    processDataAction(DataAction.OnArticlesLoadFailed(error = it))
                },
                onSuccess = {
                    processDataAction(
                        DataAction.OnArticlesLoadSucceed(
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