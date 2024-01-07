package ru.alexadler9.newsfetcher.feature.articlesscreen.domain

import dagger.hilt.android.scopes.ViewModelScoped
import ru.alexadler9.newsfetcher.base.ext.attempt
import ru.alexadler9.newsfetcher.data.NewsRepository
import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.model.ArticleModel
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
     * Get list of article bookmarks.
     */
    suspend fun getArticleBookmarks() = attempt { repository.getArticleBookmarks() }
}