package ru.alexadler9.newsfetcher.feature.articlesscreen.domain

import dagger.hilt.android.scopes.ViewModelScoped
import ru.alexadler9.newsfetcher.base.ext.attempt
import ru.alexadler9.newsfetcher.data.NewsRepository
import javax.inject.Inject

/**
 * Articles interactor.
 */
@ViewModelScoped
class ArticlesInteractor @Inject constructor(private val repository: NewsRepository) {

    /**
     * Get live top articles headlines.
     */
    suspend fun getArticles() = attempt { repository.getArticles() }
}