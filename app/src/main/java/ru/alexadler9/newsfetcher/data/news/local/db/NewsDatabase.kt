package ru.alexadler9.newsfetcher.data.news.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.alexadler9.newsfetcher.data.news.local.db.model.BookmarkEntity

const val DATABASE_NAME = "news-database"

@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao
}