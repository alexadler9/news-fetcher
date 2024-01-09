package ru.alexadler9.newsfetcher.feature.bookmarksscreen

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
    suspend fun getArticleBookmarks() = attempt { repository.getArticleBookmarks() }

    /**
     * Delete article from bookmarks.
     * @param article The article.
     */
    suspend fun deleteArticleFromBookmarks(article: ArticleModel) =
        attempt { repository.deleteArticleFromBookmarks(article) }
}