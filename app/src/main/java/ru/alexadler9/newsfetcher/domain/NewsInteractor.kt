package ru.alexadler9.newsfetcher.domain

import ru.alexadler9.newsfetcher.base.ext.attempt
import ru.alexadler9.newsfetcher.data.NewsRepository

/**
 * News interactor.
 */
class NewsInteractor(private val repository: NewsRepository) {

    /**
     * Get live top articles headlines.
     */
    suspend fun getArticles() = attempt { repository.getArticles() }
}