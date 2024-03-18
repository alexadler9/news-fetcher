package ru.alexadler9.newsfetcher.mock.prefs

import ru.alexadler9.newsfetcher.data.news.local.prefs.INewsPreferencesSource
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry

class MockingNewsPreferencesSource : INewsPreferencesSource {

    private val countryKey = "COUNTRY"
    private val categoryKey = "CATEGORY"
    private val lastUrlKey = "LAST_URL"
    private val newsPollKey = "NEWS_POLL"

    private val map = hashMapOf<String, Any>(
        countryKey to ArticlesCountry.USA.name,
        categoryKey to ArticlesCategory.GENERAL.name,
        lastUrlKey to "",
        newsPollKey to false
    )

    override fun getCountry() = map[countryKey].toString()

    override fun setCountry(country: String) {
        map[countryKey] = country
    }

    override fun getCategory() = map[categoryKey].toString()

    override fun setCategory(category: String) {
        map[categoryKey] = category
    }

    override fun getLastUrl() = map[lastUrlKey].toString()

    override fun setLastUrl(url: String) {
        map[lastUrlKey] = url
    }

    override fun newsPollEnabled() = map[newsPollKey] as Boolean

    override fun setNewsPoll(isOn: Boolean) {
        map[newsPollKey] = isOn
    }
}