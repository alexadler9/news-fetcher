package ru.alexadler9.newsfetcher.feature.newsworker

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import ru.alexadler9.newsfetcher.base.ext.parcelable

class NewsNotificationsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (resultCode != Activity.RESULT_OK) {
            // A foreground activity has canceled broadcast processing.
            return
        }

        val requestCode = intent.getIntExtra(NewsPollWorker.EXTRA_REQUEST_CODE, 0)
        val notification = intent.parcelable<Notification>(NewsPollWorker.EXTRA_NOTIFICATION)!!
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(requestCode, notification)
    }
}