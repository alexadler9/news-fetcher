package ru.alexadler9.newsfetcher.data.news.local

import androidx.room.*
import ru.alexadler9.newsfetcher.data.news.local.model.BOOKMARKS_TABLE
import ru.alexadler9.newsfetcher.data.news.local.model.BookmarkEntity

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(entity: BookmarkEntity)

    @Query("SELECT * FROM $BOOKMARKS_TABLE")
    suspend fun getBookmarks(): List<BookmarkEntity>

    @Update
    suspend fun updateBookmark(entity: BookmarkEntity)

    @Delete
    suspend fun deleteBookmark(entity: BookmarkEntity)

    @Query("SELECT EXISTS (SELECT 1 FROM $BOOKMARKS_TABLE WHERE url = :url)")
    suspend fun bookmarkExist(url: String): Boolean
}