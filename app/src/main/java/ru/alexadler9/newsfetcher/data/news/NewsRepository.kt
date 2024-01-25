package ru.alexadler9.newsfetcher.data.news

import android.graphics.Bitmap
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.alexadler9.newsfetcher.base.ext.enumContains
import ru.alexadler9.newsfetcher.data.news.local.db.NewsLocalSource
import ru.alexadler9.newsfetcher.data.news.local.prefs.NewsPreferencesSource
import ru.alexadler9.newsfetcher.data.news.remote.NewsPagingRemoteSource
import ru.alexadler9.newsfetcher.data.news.remote.NewsRemoteSource
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsRemoteSource: NewsRemoteSource,
    private val newsPagingRemoteSourceFactory: NewsPagingRemoteSource.Factory,
    private val newsLocalSource: NewsLocalSource,
    private val newsPreferencesSource: NewsPreferencesSource
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
     * Get live top articles headlines.
     * @param country The country for which the articles will be searched.
     */
    suspend fun getTopHeadlinesArticles(country: ArticlesCountry): List<ArticleModel> {
        return newsRemoteSource.getTopHeadlinesArticles(country.toRemote()).articleList.filter {
            it.title != "[Removed]" && it.description != ""
        }.map {
            it.toDomain()
        }
    }

    /**
     * Get live top articles headlines via paging source.
     * @param country The country for which the articles will be searched.
     */
    fun getTopHeadlinesArticlesPagingSource(country: ArticlesCountry): PagingSource<Int, ArticleModel> {
        return newsPagingRemoteSourceFactory.create(country.toRemote())
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
}