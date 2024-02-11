package ru.alexadler9.newsfetcher.data.news.local.db

import kotlinx.coroutines.flow.Flow
import ru.alexadler9.newsfetcher.data.news.local.db.model.BookmarkEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Source for accessing the local news data.
 */
@Singleton
class NewsLocalSource @Inject constructor(private val bookmarkDao: BookmarkDao) {

    /**
     * Add bookmark. If a bookmark with the given article URL already exists, it will be ignored.
     * @param entity The bookmark.
     */
    suspend fun addBookmark(entity: BookmarkEntity) {
        bookmarkDao.addBookmark(entity)
    }

    /**
     * Get list of bookmarks.
     */
    fun getBookmarks(): Flow<List<BookmarkEntity>> {
        return bookmarkDao.getBookmarks()
    }

    /**
     * Delete bookmark.
     * @param entity The bookmark.
     */
    suspend fun deleteBookmark(entity: BookmarkEntity) {
        bookmarkDao.deleteBookmark(entity)
    }

    /**
     * Check if a bookmark with the specified article URL exists.
     * @param url The article URL.
     */
    suspend fun bookmarkExist(url: String): Boolean {
        return bookmarkDao.bookmarkExist(url)
    }
}