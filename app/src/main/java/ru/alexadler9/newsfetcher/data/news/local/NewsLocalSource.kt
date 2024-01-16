package ru.alexadler9.newsfetcher.data.news.local

import ru.alexadler9.newsfetcher.data.news.local.model.BookmarkEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Source for accessing the local news data.
 */
@Singleton
class NewsLocalSource @Inject constructor(private val bookmarkDao: BookmarkDao) {

    /**
     * Add bookmark. If a bookmark with the given article URL already exists, it will be replaced.
     * @param entity The bookmark.
     */
    suspend fun addBookmark(entity: BookmarkEntity) {
        bookmarkDao.addBookmark(entity)
    }

    /**
     * Get list of bookmarks.
     */
    suspend fun getBookmarks(): List<BookmarkEntity> {
        return bookmarkDao.getBookmarks()
    }

    /**
     * Update bookmark.
     * @param entity The bookmark.
     */
    suspend fun updateBookmark(entity: BookmarkEntity) {
        bookmarkDao.updateBookmark(entity)
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