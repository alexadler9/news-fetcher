package ru.alexadler9.newsfetcher.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.alexadler9.newsfetcher.data.news.local.prefs.INewsPreferencesSource
import ru.alexadler9.newsfetcher.data.news.local.prefs.NewsPreferencesSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PrefsModule {

    @Singleton
    @Provides
    fun provideNewsPreferencesSource(@ApplicationContext appContext: Context): INewsPreferencesSource {
        return NewsPreferencesSource(appContext)
    }
}