package ru.alexadler9.newsfetcher

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import ru.alexadler9.newsfetcher.mock.network.RESPONSE_DELAY
import ru.alexadler9.newsfetcher.utility.RecyclerViewChild.Companion.actionOnChildAtPosition
import ru.alexadler9.newsfetcher.utility.RecyclerViewChild.Companion.childAtPositionWithMatcher
import ru.alexadler9.newsfetcher.utility.RecyclerViewItemCountAssertion
import ru.alexadler9.newsfetcher.utility.waitFor
import ru.alexadler9.newsfetcher.utility.withDrawableId

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ArticlesFragmentInstrumentedTest : TestActivity() {

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
        onView(withId(R.id.rvArticles))
            .check(matches(isDisplayed()))
            .check(RecyclerViewItemCountAssertion(equalTo(0)))

        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.pbArticles)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rvArticles))
            .check(matches(isDisplayed()))
            .check(RecyclerViewItemCountAssertion(greaterThanOrEqualTo(FIRST_PAGE_ITEMS_COUNT)))
            .check(
                matches(
                    allOf(
                        childAtPositionWithMatcher(
                            0,
                            R.id.tvAuthor,
                            withText(ARTICLE_FIRST.author)
                        ),
                        childAtPositionWithMatcher(0, R.id.tvDate, withText("23.01.2024 20:53")),
                        childAtPositionWithMatcher(
                            0,
                            R.id.tvTitle,
                            withText(containsString(ARTICLE_FIRST.title))
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

    @Test
    fun testArticlesQueryAppliedSuccessfully() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.rvArticles))
            .check(matches(isDisplayed()))
            .check(RecyclerViewItemCountAssertion(greaterThanOrEqualTo(FIRST_PAGE_ITEMS_COUNT)))

        onView(withId(R.id.menuItemSearch))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .check(matches(isDisplayed()))
            .perform(clearText(), typeText("query"), pressImeActionButton())

        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        // Taking into account a possible progress item
        onView(withId(R.id.rvArticles))
            .check(RecyclerViewItemCountAssertion(lessThanOrEqualTo(QUERY_PAGE_ITEMS_COUNT + 1)))
            .check(
                matches(
                    allOf(
                        childAtPositionWithMatcher(
                            0,
                            R.id.tvAuthor,
                            withText(ARTICLE_QUERY.author)
                        ),
                        childAtPositionWithMatcher(0, R.id.tvDate, withText("23.01.2024 19:45")),
                        childAtPositionWithMatcher(
                            0,
                            R.id.tvTitle,
                            withText(containsString(ARTICLE_QUERY.title))
                        ),
                    )
                )
            )
    }

    @Test
    fun testArticlesLoadedUnsuccessfully() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.menuItemSearch))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .check(matches(isDisplayed()))
            .perform(
                clearText(),
                // Testing agreement: code 400 will be issued for the "bad-request" query.
                typeText("bad-request"),
                pressImeActionButton()
            )

        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.rvArticles))
            .check(matches(isDisplayed()))
            .check(RecyclerViewItemCountAssertion(equalTo(0)))
        onView((withId(R.id.layoutError))).check(matches(isDisplayed()))
        onView((withId(R.id.tvError)))
            .check(matches(isDisplayed()))
            .check(matches(withText(containsString("HTTP 400"))))
    }

    @Test
    fun testNoArticles() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.menuItemSearch))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .check(matches(isDisplayed()))
            .perform(
                clearText(),
                typeText("empty"),
                pressImeActionButton()
            )

        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.rvArticles))
            .check(RecyclerViewItemCountAssertion(equalTo(0)))
        onView(withId(R.id.layoutEmpty)).check(matches(isDisplayed()))
    }

    @Test
    fun testAddArticleToBookmark() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.rvArticles))
            .check(matches(isDisplayed()))
            .check(
                matches(
                    childAtPositionWithMatcher(
                        0,
                        R.id.ivBookmark,
                        withDrawableId(R.drawable.ic_baseline_bookmark_border_24)
                    )
                )
            )

        onView(withId(R.id.rvArticles))
            .perform(actionOnChildAtPosition<RecyclerView.ViewHolder>(0, R.id.ivBookmark, click()))

        onView(withId(R.id.rvArticles))
            .check(matches(isDisplayed()))
            .check(
                matches(
                    childAtPositionWithMatcher(
                        0,
                        R.id.ivBookmark,
                        withDrawableId(R.drawable.ic_baseline_bookmark_24)
                    )
                )
            )
    }

    @Test
    fun testDeleteArticleFromBookmarks() {
        onView(isRoot()).perform(waitFor(RESPONSE_DELAY))

        onView(withId(R.id.rvArticles))
            .perform(actionOnChildAtPosition<RecyclerView.ViewHolder>(0, R.id.ivBookmark, click()))

        onView(withId(R.id.rvArticles))
            .perform(actionOnChildAtPosition<RecyclerView.ViewHolder>(0, R.id.ivBookmark, click()))

        onView(withId(R.id.rvArticles))
            .check(matches(isDisplayed()))
            .check(
                matches(
                    childAtPositionWithMatcher(
                        0,
                        R.id.ivBookmark,
                        withDrawableId(R.drawable.ic_baseline_bookmark_border_24)
                    )
                )
            )
    }

    companion object {

        const val FIRST_PAGE_ITEMS_COUNT = 15
        const val QUERY_PAGE_ITEMS_COUNT = 1
    }
}