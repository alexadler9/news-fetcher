package ru.alexadler9.newsfetcher

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.alexadler9.newsfetcher.mock.RESPONSE_DELAY
import ru.alexadler9.newsfetcher.utility.RecyclerViewChildActions.Companion.childAtPositionWithMatcher
import ru.alexadler9.newsfetcher.utility.waitFor

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testArticlesFragmentDisplayed() {
        onView(
            allOf(
                withId(R.id.articlesFragment),
                isDescendantOfA(withId(R.id.fragmentContainerView))
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun testArticlesLoadedSuccessfully() {
        onView(withId(R.id.pbArticles)).check(matches(isDisplayed()))
        onView(withId(R.id.rvArticles)).check(matches(not(isDisplayed())))

        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.pbArticles)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rvArticles)).check(matches(isDisplayed()))

        onView(withId(R.id.rvArticles))
            .check(
                matches(
                    allOf(
                        childAtPositionWithMatcher(0, R.id.tvAuthor, withText("Christopher Spata")),
                        childAtPositionWithMatcher(0, R.id.tvDate, withText("23.01.2024 20:53")),
                        childAtPositionWithMatcher(
                            0,
                            R.id.tvTitle,
                            withText(containsString("opening monologues"))
                        ),
                    )
                )
            )
    }

    @Test
    fun testArticlesIntermediateLoadingSuccessful() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.rvArticles))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToLastPosition<RecyclerView.ViewHolder>())
            .perform(RecyclerViewActions.scrollToLastPosition<RecyclerView.ViewHolder>())

        onView(
            allOf(
                withId(R.id.pbLoading),
                isDescendantOfA(withId(R.id.rvArticles))
            )
        ).check(matches(isDisplayed()))
    }
}