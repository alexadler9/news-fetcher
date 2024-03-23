package ru.alexadler9.newsfetcher.data.news.local.prefs

import co.windly.ktxprefs.annotation.DefaultBoolean
import co.windly.ktxprefs.annotation.DefaultString
import co.windly.ktxprefs.annotation.Prefs

@Prefs(value = "NewsPreferences")
class NewsPreferences(
    /** The country for which the articles will be searched. */
    @DefaultString(value = "")
    internal val country: String,

    /** The category in which the articles will be searched. */
    @DefaultString(value = "")
    internal val category: String,

    /** Last received URL to the article. */
    @DefaultString(value = "")
    internal val lastUrl: String,

    /** Flag indicating whether detection of new articles in the background is enabled. */
    @DefaultBoolean(value = false)
    internal val newsPollEnabled: Boolean
)