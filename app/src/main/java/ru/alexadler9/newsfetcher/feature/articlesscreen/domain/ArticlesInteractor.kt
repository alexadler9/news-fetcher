package ru.alexadler9.newsfetcher.feature.articlesscreen.domain

import ru.alexadler9.newsfetcher.base.ext.attempt
import ru.alexadler9.newsfetcher.data.NewsRepository
import ru.alexadler9.newsfetcher.di.scopes.AppScope
import javax.inject.Inject

/**
 * Articles interactor.
 */
@AppScope
class ArticlesInteractor @Inject constructor(private val repository: NewsRepository) {

    /**
     * Get live top articles headlines.
     */
    suspend fun getArticles() = attempt { repository.getArticles() }
}