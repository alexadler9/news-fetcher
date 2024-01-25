package ru.alexadler9.newsfetcher.data.news

import android.graphics.Bitmap
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.alexadler9.newsfetcher.data.news.local.db.NewsLocalSource
import ru.alexadler9.newsfetcher.data.news.remote.NewsPagingRemoteSource
import ru.alexadler9.newsfetcher.data.news.remote.NewsRemoteSource
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsRemoteSource: NewsRemoteSource,
    private val newsPagingRemoteSource: NewsPagingRemoteSource,
    private val newsLocalSource: NewsLocalSource
) {

    /**
     * Get live top articles headlines.
     */
    suspend fun getTopHeadlinesArticles(): List<ArticleModel> {
        return newsRemoteSource.getTopHeadlinesArticles().articleList.filter {
            it.title != "[Removed]" && it.description != ""
        }.map {
            it.toDomain()
        }
    }

    /**
     * Get live top articles headlines via paging source.
     */
    fun getTopHeadlinesArticlesPagingSource(): PagingSource<Int, ArticleModel> {
        return newsPagingRemoteSource
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