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
     * Get list of article bookmarks.
     */
    fun getArticleBookmarks() = repository.getArticleBookmarks()

    /**
     * Add article to bookmarks if it is not there, otherwise delete it.
     * @param article The article.
     */
    suspend fun changeArticleBookmark(article: ArticleModel) =
        attempt {
            if (repository.articleBookmarkExist(article.url)) {
                repository.deleteArticleFromBookmarks(article)
            } else {
                repository.addArticleToBookmark(article)
            }
        }
}