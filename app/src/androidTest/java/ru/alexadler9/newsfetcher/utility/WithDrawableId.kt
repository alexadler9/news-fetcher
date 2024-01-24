package ru.alexadler9.newsfetcher.utility

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Checks that the ImageView contains a drawable with target id.
 */
fun withDrawableId(
    @DrawableRes drawableId: Int
): Matcher<View> {
    return object : TypeSafeMatcher<View>(View::class.java) {
        override fun describeTo(description: Description) {
            description.appendText(
                "checks that the view contains a drawable with id $drawableId"
            )
        }

        override fun matchesSafely(target: View): Boolean {
            val drawable: Drawable? = when (target) {
                is ImageView -> target.drawable
                else -> null
            }
            requireNotNull(drawable)

            val resources: Resources = target.context.resources
            val expectedDrawable: Drawable? =
                resources.getDrawable(drawableId, target.context.theme)
            return expectedDrawable?.pixelsEqualTo(drawable) ?: false
        }
    }
}