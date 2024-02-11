package ru.alexadler9.newsfetcher.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import ru.alexadler9.newsfetcher.data.news.local.prefs.INewsPreferencesSource
import ru.alexadler9.newsfetcher.mock.prefs.MockingNewsPreferencesSource
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PrefsModule::class]
)
class FakePrefsModule {

    @Singleton
    @Provides
    fun provideNewsPreferencesSource(): INewsPreferencesSource {
        return MockingNewsPreferencesSource()
    }
}