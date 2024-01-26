package ru.alexadler9.newsfetcher.data.news.local.prefs

import co.windly.ktxprefs.annotation.DefaultString
import co.windly.ktxprefs.annotation.Prefs

@Prefs(value = "NewsPreferences")
class NewsPreferences(
    /** The country for which the articles will be searched. */
    @DefaultString(value = "")
    internal val country: String,

    /** The category in which the articles will be searched. */
    @DefaultString(value = "")
    internal val category: String
)