package ru.alexadler9.newsfetcher.feature.newsworker

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.Fragment

/**
 * A fragment subclass that performs registration and unregistration of a dynamic BroadcastReceiver,
 * which receives a message only when the fragment is visible to the user.
 */
abstract class NewsNotificationsReceiverFragment : Fragment() {

    /**
     * The receiver is used in conjunction with a static receiver declared in the manifest:
     * it cancels further processing of the message (sending a notification in this case)
     * if the fragment is visible.
     */
    private val onShowNewsNotification = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // If we receive this broadcast message, then there is no need to display the notification.
            resultCode = Activity.RESULT_CANCELED
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(NewsPollWorker.ACTION_SHOW_NEWS_NOTIFICATION)
        requireActivity().registerReceiver(
            onShowNewsNotification,
            filter,
            NewsPollWorker.PERM_PRIVATE_NEWS_NOTIFICATIONS,
            null
        )
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(onShowNewsNotification)
    }
}