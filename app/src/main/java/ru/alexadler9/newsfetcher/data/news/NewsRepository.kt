package ru.alexadler9.newsfetcher.data.news

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.base.ext.enumContains
import ru.alexadler9.newsfetcher.data.news.local.db.NewsLocalSource
import ru.alexadler9.newsfetcher.data.news.local.prefs.INewsPreferencesSource
import ru.alexadler9.newsfetcher.data.news.remote.NewsPagingRemoteSource
import ru.alexadler9.newsfetcher.data.news.remote.NewsRemoteSource
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsRemoteSource: NewsRemoteSource,
    private val newsPagingRemoteSourceFactory: NewsPagingRemoteSource.Factory,
    private val newsLocalSource: NewsLocalSource,
    private val newsPreferencesSource: INewsPreferencesSource
) {

    /**
     * Get the country for which the articles will be searched.
     */
    fun getArticlesCountry(): ArticlesCountry {
        val country = newsPreferencesSource.getCountry()
        return if (enumContains<ArticlesCountry>(country))
            ArticlesCountry.valueOf(country) else
            ArticlesCountry.values().first()
    }

    /**
     * Save the country for which the articles will be searched.
     * @param country The country.
     */
    fun saveArticlesCountry(country: ArticlesCountry) {
        newsPreferencesSource.setCountry(country.name)
    }

    /**
     * Get the category in which the articles will be searched.
     */
    fun getArticlesCategory(): ArticlesCategory {
        val category = newsPreferencesSource.getCategory()
        return if (enumContains<ArticlesCategory>(category))
            ArticlesCategory.valueOf(category) else
            ArticlesCategory.GENERAL
    }

    /**
     * Save the category in which the articles will be searched.
     * @param category The category.
     */
    fun saveArticlesCategory(category: ArticlesCategory) {
        newsPreferencesSource.setCategory(category.name)
    }

    /**
     * Get last received URL to the article.
     */
    fun getLastArticleUrl() = newsPreferencesSource.getLastUrl()

    /**
     * Save last received URL to the article.
     * @param url URL to the article.
     */
    fun saveLastArticleUrl(url: String) {
        newsPreferencesSource.setLastUrl(url)
    }

    /**
     * Get live top articles headlines.
     * @param country The country for which the articles will be searched.
     * @param category The category in which the articles will be searched.
     * @param query Keywords or a phrase by which the articles will be searched.
     */
    suspend fun getTopHeadlinesArticles(
        country: ArticlesCountry,
        category: ArticlesCategory,
        query: String
    ): List<ArticleModel> {
        return newsRemoteSource.getTopHeadlinesArticles(
            country.toRemote(),
            category.toRemote(),
            query
        ).articleList.filter {
            it.title != "[Removed]" && it.description != ""
        }.map {
            it.toDomain()
        }
    }

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
    ): PagingSource<Int, ArticleModel> {
        return newsPagingRemoteSourceFactory.create(
            country.toRemote(),
            category.toRemote(),
            query
        )
    }

    /**
     * Get a wallpaper image for article.
     * @param article The article.
     */
    suspend fun getArticleWallpaper(article: ArticleModel): Bitmap {
        return newsRemoteSource.getArticleWallpaper(article.urlToImage)
    }

    /**
     * Add article to bookmarks. If a bookmark with the given article URL already exists, it will be ignored.
     * @param article The article.
     */
    suspend fun addArticleToBookmark(article: ArticleModel) {
        newsLocalSource.addBookmark(article.toEntity())
    }

    /**
     * Get list of article bookmarks. It is sorted from newest to oldest.
     */
    fun getArticleBookmarks(): Flow<List<ArticleModel>> {
        return newsLocalSource.getBookmarks().map {
            it.map { bookmarkEntity ->
                bookmarkEntity.toDomain()
            }.toList()
        }
    }

    /**
     * Delete article from bookmarks.
     * @param article The article.
     */
    suspend fun deleteArticleFromBookmarks(article: ArticleModel) {
        newsLocalSource.deleteBookmark(article.toEntity())
    }

    /**
     * Check if a bookmark with the specified article URL exists.
     * @param url The article URL.
     */
    suspend fun articleBookmarkExist(url: String): Boolean {
        return newsLocalSource.bookmarkExist(url)
    }

    /**
     * Share article in messengers.
     * The message will be generated from the title and URL of the article.
     * @param context The context.
     * @param article The article.
     */
    fun shareArticle(context: Context, article: ArticleModel) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.article_link_share_message, article.title, article.url)
            )
        }.also { intent ->
            val chooserIntent = Intent.createChooser(
                intent,
                context.getString(R.string.article_link_share_title)
            )
            context.startActivity(chooserIntent)
        }
    }

    /**
     * Open original article in browser.
     * @param context The context.
     * @param article The article.
     */
    fun openArticleInBrowser(context: Context, article: ArticleModel) {
        Intent(Intent.ACTION_VIEW, Uri.parse(article.url)).apply {
            context.startActivity(this)
        }
    }
}