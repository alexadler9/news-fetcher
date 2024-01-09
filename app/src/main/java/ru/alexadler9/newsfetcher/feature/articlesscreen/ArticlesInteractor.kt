package ru.alexadler9.newsfetcher.feature.articlesscreen

import dagger.hilt.android.scopes.ViewModelScoped
import ru.alexadler9.newsfetcher.base.ext.attempt
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import javax.inject.Inject

/**
 * Articles interactor.
 */
@ViewModelScoped
class ArticlesInteractor @Inject constructor(private val repository: NewsRepository) {

    /**
     * Get live top articles headlines.
     */
    suspend fun getArticles() = attempt { repository.getArticles() }

    /**
     * Add article to bookmarks. If a bookmark with the given article URL already exists, it will be replaced.
     * @param article The article.
     */
    suspend fun addArticleToBookmark(article: ArticleModel) =
        attempt { repository.addArticleToBookmark(article) }

    /**
     * Delete article from bookmarks.
     * @param article The article.
     */
    suspend fun deleteArticleFromBookmarks(article: ArticleModel) =
        attempt { repository.deleteArticleFromBookmarks(article) }

    /**
     * Check if a bookmark with the specified article URL exists.
     * @param url The article URL.
     */
    suspend fun articleBookmarkExist(url: String) = attempt { repository.articleBookmarkExist(url) }
}