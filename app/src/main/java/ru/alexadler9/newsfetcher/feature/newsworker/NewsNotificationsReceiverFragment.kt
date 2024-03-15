package ru.alexadler9.newsfetcher.feature.newsworker

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.Fragment

abstract class NewsNotificationsReceiverFragment : Fragment() {

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