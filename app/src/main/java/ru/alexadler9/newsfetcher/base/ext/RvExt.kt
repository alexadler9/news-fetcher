package ru.alexadler9.newsfetcher.base.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Reset the adapter when detached from the window.
 * Otherwise it contains a reference to the RecyclerView, which leads to a leak.
 */
fun RecyclerView.setAdapterAndCleanupOnDetachFromWindow(recyclerViewAdapter: RecyclerView.Adapter<*>) {
    adapter = recyclerViewAdapter
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

        override fun onViewDetachedFromWindow(v: View) {
            adapter = null
            removeOnAttachStateChangeListener(this)
        }

        override fun onViewAttachedToWindow(v: View) {
        }
    })
}