package ru.alexadler9.newsfetcher.data.news

import android.graphics.Bitmap
import ru.alexadler9.newsfetcher.data.local.NewsLocalSource
import ru.alexadler9.newsfetcher.data.remote.NewsRemoteSource
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val remoteSource: NewsRemoteSource,
    private val localSource: NewsLocalSource
) {

    /**
     * Get live top articles headlines.
     */
    suspend fun getArticles(): List<ArticleModel> {
        return remoteSource.getArticles().articleList.filter {
            it.title != "[Removed]"
        }.map {
            it.toDomain()
        }
    }

    /**
     * Get a wallpaper image for article.
     * @param article The article.
     */
    suspend fun getArticleWallpaper(article: ArticleModel): Bitmap {
        return remoteSource.getArticleWallpaper(article.urlToImage)
    }

    /**
     * Add article to bookmarks. If a bookmark with the given article URL already exists, it will be replaced.
     * @param article The article.
     */
    suspend fun addArticleToBookmark(article: ArticleModel) {
        localSource.addBookmark(article.toEntity())
    }

    /**
     * Get list of article bookmarks.
     */
    suspend fun getArticleBookmarks(): List<ArticleModel> {
        return localSource.getBookmarks().map {
            it.toDomain()
        }
    }

    /**
     * Update article bookmark.
     * @param article The article.
     */
    suspend fun updateArticleBookmark(article: ArticleModel) {
        localSource.updateBookmark(article.toEntity())
    }

    /**
     * Delete article from bookmarks.
     * @param article The article.
     */
    suspend fun deleteArticleFromBookmarks(article: ArticleModel) {
        localSource.deleteBookmark(article.toEntity())
    }

    /**
     * Check if a bookmark with the specified article URL exists.
     * @param url The article URL.
     */
    suspend fun articleBookmarkExist(url: String): Boolean {
        return localSource.bookmarkExist(url)
    }
}