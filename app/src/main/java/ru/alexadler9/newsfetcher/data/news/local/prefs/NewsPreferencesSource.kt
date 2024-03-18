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

    /**
     * Get last received URL to the article.
     */
    override fun getLastUrl() = appContext.requireNewsPreferences().getLastUrl()

    /**
     * Save last received URL to the article.
     * @param url URL to the article.
     */
    override fun setLastUrl(url: String) {
        appContext.requireNewsPreferences().setLastUrl(url)
    }

    /**
     * Get flag indicating whether detection of new articles in the background is enabled.
     */
    override fun newsPollEnabled() = appContext.requireNewsPreferences().getNewsPollEnabled()

    /**
     * Save the flag indicating whether detection of new articles in the background is enabled.
     * @param isOn Flag state.
     */
    override fun setNewsPoll(isOn: Boolean) {
        appContext.requireNewsPreferences().setNewsPollEnabled(isOn)
    }
}