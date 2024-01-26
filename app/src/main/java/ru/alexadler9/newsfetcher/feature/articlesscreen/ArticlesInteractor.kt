package ru.alexadler9.newsfetcher.feature.articlesscreen

import dagger.hilt.android.scopes.ViewModelScoped
import ru.alexadler9.newsfetcher.base.ext.attempt
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry
import javax.inject.Inject

/**
 * Articles interactor.
 */
@ViewModelScoped
class ArticlesInteractor @Inject constructor(private val repository: NewsRepository) {

    /**
     * Get the country for which the articles will be searched.
     */
    fun getArticlesCountry() = repository.getArticlesCountry()

    /**
     * Get the category in which the articles will be searched.
     */
    fun getArticlesCategory() = repository.getArticlesCategory()

    /**
     * Get live top articles headlines via paging source.
     * @param country The country for which the articles will be searched.
     * @param category The category in which the articles will be searched.
     * @param query Keywords or a phrase by which the articles will be searched.
     */
    fun getTopHeadlinesArticlesPagingSource(
        country: ArticlesCountry,
        category: ArticlesCategory,
        query: String
    ) = repository.getTopHeadlinesArticlesPagingSource(country, category, query)

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