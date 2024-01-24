package ru.alexadler9.newsfetcher.utility

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher

class RecyclerViewChildActions {

    companion object {

        /**
         * Checks that the childMatcher matches a child view
         * inside a RecyclerView's item.
         */
        fun childAtPositionWithMatcher(
            itemPosition: Int,
            childId: Int,
            childMatcher: Matcher<View>
        ): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText(
                        "Checks that the childMatcher matches a child view " +
                                " inside a RecyclerView's $itemPosition item"
                    )
                }

                override fun matchesSafely(recyclerView: RecyclerView?): Boolean {
                    val viewHolder =
                        recyclerView?.findViewHolderForAdapterPosition(itemPosition)
                    val matcher = ViewMatchers.hasDescendant(
                        CoreMatchers.allOf(
                            ViewMatchers.withId(childId),
                            childMatcher
                        )
                    )
                    return viewHolder != null && matcher.matches(viewHolder.itemView)
                }
            }
        }
    }
}