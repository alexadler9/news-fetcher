package ru.alexadler9.newsfetcher.utility

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class RecyclerViewChild {

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
                        "checks that the childMatcher matches a child view " +
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

        /**
         * Perform action on the child view inside a RecyclerView's item.
         */
        fun <VH : RecyclerView.ViewHolder?> actionOnChildAtPosition(
            itemPosition: Int,
            childId: Int,
            action: ViewAction,
        ): ViewAction {
            return object : ViewAction {
                override fun getDescription(): String {
                    return "perform action on the child view inside a RecyclerView's $itemPosition item"
                }

                override fun getConstraints(): Matcher<View> {
                    return allOf(isDisplayed(), isAssignableFrom(View::class.java))
                }

                override fun perform(uiController: UiController, view: View) {
                    val recyclerView = view as RecyclerView

                    @Suppress("UNCHECKED_CAST")
                    val viewHolderForPosition =
                        recyclerView.findViewHolderForAdapterPosition(itemPosition) as VH
                            ?: throw PerformException.Builder()
                                .withActionDescription(this.toString())
                                .withViewDescription(HumanReadables.describe(view))
                                .withCause(IllegalStateException("no view holder at position: $itemPosition"))
                                .build()

                    val viewAtPosition = viewHolderForPosition.itemView
                    viewAtPosition.let {
                        val child: View = it.findViewById(childId)
                        action.perform(uiController, child)
                    }
                }
            }
        }
    }
}