package ru.alexadler9.newsfetcher.data.news.local.prefs

interface INewsPreferencesSource {

    fun getCountry(): String

    fun setCountry(country: String)

    fun getCategory(): String

    fun setCategory(category: String)

    fun getLastUrl(): String

    fun setLastUrl(url: String)
}