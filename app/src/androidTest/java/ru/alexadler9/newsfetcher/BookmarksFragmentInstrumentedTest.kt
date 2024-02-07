package ru.alexadler9.newsfetcher

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import ru.alexadler9.newsfetcher.mock.network.RESPONSE_DELAY
import ru.alexadler9.newsfetcher.utility.RecyclerViewChild.Companion.actionOnChildAtPosition
import ru.alexadler9.newsfetcher.utility.RecyclerViewItemCountAssertion
import ru.alexadler9.newsfetcher.utility.waitFor

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BookmarksFragmentInstrumentedTest : TestActivity() {

    @Test
    fun testBookmarksFragmentDisplayed() {
        toBookmarksFragment()

        onView(
            allOf(
                withId(R.id.bookmarksFragment),
                isDescendantOfA(withId(R.id.fragmentContainerView))
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun testNoBookmarkedArticles() {
        toBookmarksFragment()

        onView(withId(R.id.rvBookmarks))
            .check(RecyclerViewItemCountAssertion(equalTo(0)))
        onView(withId(R.id.layoutEmpty)).check(matches(isDisplayed()))
    }

    @Test
    fun testBookmarkedArticlesLoadedSuccessfully() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        addThreeArticlesToBookmarks()
        toBookmarksFragment()

        onView(withId(R.id.rvBookmarks))
            .check(RecyclerViewItemCountAssertion(equalTo(3)))
        onView(withId(R.id.layoutEmpty)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testDeleteArticleFromBookmarks() {
        addThreeArticlesToBookmarks()
        toBookmarksFragment()

        onView(withId(R.id.rvBookmarks))
            .perform(actionOnChildAtPosition<RecyclerView.ViewHolder>(0, R.id.ivBookmark, click()))

        onView(withId(R.id.rvBookmarks))
            .check(RecyclerViewItemCountAssertion(equalTo(2)))
        onView(withId(R.id.layoutEmpty)).check(matches(not(isDisplayed())))
    }

    private fun addThreeArticlesToBookmarks() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        for (item in 0 until 3)
            onView(withId(R.id.rvArticles))
                .perform(
                    actionOnChildAtPosition<RecyclerView.ViewHolder>(
                        item,
                        R.id.ivBookmark,
                        click()
                    )
                )
    }

    private fun toBookmarksFragment() {
        onView(
            allOf(
                withId(R.id.bookmarksFragment),
                isDescendantOfA(withId(R.id.bottomNav))
            )
        ).perform(click())
    }
}