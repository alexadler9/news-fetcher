package ru.alexadler9.newsfetcher.feature.bookmarksscreen

import android.content.Context
import dagger.hilt.android.scopes.ViewModelScoped
import ru.alexadler9.newsfetcher.base.ext.attempt
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import javax.inject.Inject

/**
 * Bookmarks interactor.
 */
@ViewModelScoped
class BookmarksInteractor @Inject constructor(private val repository: NewsRepository) {

    /**
     * Get list of article bookmarks.
     */
    fun getArticleBookmarks() = repository.getArticleBookmarks()

    /**
     * Delete article from bookmarks.
     * @param article The article.
     */
    suspend fun deleteArticleFromBookmarks(article: ArticleModel) =
        attempt { repository.deleteArticleFromBookmarks(article) }

    /**
     * Share article in messengers.
     * The message will be generated from the title and URL of the article.
     * @param context The context.
     * @param article The article.
     */
    fun shareArticle(context: Context, article: ArticleModel) {
        repository.shareArticle(context, article)
    }

    /**
     * Open original article in browser.
     * @param context The context.
     * @param article The article.
     */
    fun openArticleInBrowser(context: Context, article: ArticleModel) {
        repository.openArticleInBrowser(context, article)
    }
}