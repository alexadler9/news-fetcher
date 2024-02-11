package ru.alexadler9.newsfetcher.data.news.local.prefs

import android.content.Context

/**
 * Source for accessing news settings.
 */
class NewsPreferencesSource(private val appContext: Context) : INewsPreferencesSource {

    /**
     * Get country for which the articles will be searched.
     */
    override fun getCountry() = appContext.requireNewsPreferences().getCountry()

    /**
     * Save the country for which the articles will be searched.
     * @param country The country.
     */
    override fun setCountry(country: String) {
        appContext.requireNewsPreferences().setCountry(country)
    }

    /**
     * Get category in which the articles will be searched.
     */
    override fun getCategory() = appContext.requireNewsPreferences().getCategory()

    /**
     * Save the category in which the articles will be searched.
     * @param category The category.
     */
    override fun setCategory(category: String) {
        appContext.requireNewsPreferences().setCategory(category)
    }
}