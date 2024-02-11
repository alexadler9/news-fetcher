package ru.alexadler9.newsfetcher.feature.settingsscreen

import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry
import javax.inject.Inject

/**
 * Settings interactor.
 */
class SettingsInteractor @Inject constructor(private val repository: NewsRepository) {

    /**
     * Get the country for which the articles will be searched.
     */
    fun getArticlesCountry() = repository.getArticlesCountry()

    /**
     * Get the category in which the articles will be searched.
     */
    fun getArticlesCategory() = repository.getArticlesCategory()

    /**
     * Save the country for which the articles will be searched.
     * @param country The country.
     */
    fun saveArticlesCountry(country: ArticlesCountry) = repository.saveArticlesCountry(country)

    /**
     * Save the category in which the articles will be searched.
     * @param category The category.
     */
    fun saveArticlesCategory(category: ArticlesCategory) = repository.saveArticlesCategory(category)
}