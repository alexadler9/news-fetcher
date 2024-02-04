package ru.alexadler9.newsfetcher.mock.prefs

import ru.alexadler9.newsfetcher.data.news.local.prefs.INewsPreferencesSource
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry

class MockingNewsPreferencesSource : INewsPreferencesSource {

    private val countryKey = "COUNTRY"
    private val categoryKey = "CATEGORY"

    private val map = hashMapOf<String, Any>(
        countryKey to ArticlesCountry.USA.name,
        categoryKey to ArticlesCategory.GENERAL.name
    )

    override fun getCountry() = map[countryKey].toString()

    override fun setCountry(country: String) {
        map[countryKey] = country
    }

    override fun getCategory() = map[categoryKey].toString()

    override fun setCategory(category: String) {
        map[categoryKey] = category
    }
}