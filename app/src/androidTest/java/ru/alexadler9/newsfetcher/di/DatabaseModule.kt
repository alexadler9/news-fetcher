package ru.alexadler9.newsfetcher.di

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import ru.alexadler9.newsfetcher.data.news.local.db.BookmarkDao
import ru.alexadler9.newsfetcher.data.news.local.db.NewsDatabase
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class FakeDatabaseModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(): NewsDatabase {
        return Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            NewsDatabase::class.java
        ).build()
    }

    @Provides
    fun provideBookmarkDao(database: NewsDatabase): BookmarkDao {
        return database.bookmarkDao()
    }
}