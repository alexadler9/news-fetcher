package ru.alexadler9.newsfetcher

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.alexadler9.newsfetcher.mock.network.RESPONSE_DELAY
import ru.alexadler9.newsfetcher.utility.RecyclerViewChild.Companion.actionOnChildAtPosition
import ru.alexadler9.newsfetcher.utility.waitFor

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DetailsFragmentInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testDetailsFragmentDisplayed() {
        toDetailsFragment()

        onView(
            Matchers.allOf(
                withId(R.id.detailsFragment),
                isDescendantOfA(withId(R.id.fragmentContainerView))
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun testArticlesDetailsDisplayedSuccessfully() {
        toDetailsFragment()

        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.tvDetailsAuthor))
            .check(matches(isDisplayed()))
            .check(matches(withText(ARTICLE_FIRST.author)))

        onView(withId(R.id.tvDetailsDate))
            .check(matches(isDisplayed()))
            .check(matches(withText("23.01.2024 20:53")))

        onView(withId(R.id.tvDescription))
            .check(matches(isDisplayed()))
            .check(matches(withText(ARTICLE_FIRST.description)))
    }

    private fun toDetailsFragment() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.rvArticles))
            .perform(actionOnChildAtPosition<RecyclerView.ViewHolder>(0, R.id.tvAuthor, click()))
    }
}