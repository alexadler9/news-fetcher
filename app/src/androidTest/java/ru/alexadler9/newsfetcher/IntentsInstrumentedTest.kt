package ru.alexadler9.newsfetcher

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.alexadler9.newsfetcher.mock.network.RESPONSE_DELAY
import ru.alexadler9.newsfetcher.utility.RecyclerViewChild.Companion.actionOnChildAtPosition
import ru.alexadler9.newsfetcher.utility.waitFor

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class IntentsInstrumentedTest : TestActivity() {

    @get:Rule
    val intentsRule = IntentsRule()

    @Test
    fun testArticlesOpenedInBrowserSuccessfully() {
        intending(toPackage(Intent.ACTION_VIEW))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    Intent().apply { putExtra("result", "OK") }
                )
            )

        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.rvArticles))
            .perform(actionOnChildAtPosition<RecyclerView.ViewHolder>(0, R.id.ivBrowser, click()))

        intended(hasData(Uri.parse(ARTICLE_FIRST.url)))
        intended(hasAction(Intent.ACTION_VIEW))
    }

    @Test
    fun testChooserStartedSuccessfully() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.rvArticles))
            .perform(actionOnChildAtPosition<RecyclerView.ViewHolder>(0, R.id.ivShare, click()))

        intended(hasAction(Intent.ACTION_CHOOSER))
    }
}