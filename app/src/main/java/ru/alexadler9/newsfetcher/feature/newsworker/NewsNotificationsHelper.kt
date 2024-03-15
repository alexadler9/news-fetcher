package ru.alexadler9.newsfetcher.feature.newsworker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ru.alexadler9.newsfetcher.MainActivity
import ru.alexadler9.newsfetcher.R

private const val CHANNEL_ID = "NewArticlesChannel"
private const val CHANNEL_NAME = "NewArticlesChannelName"
private const val CHANNEL_DESCRIPTION = "NewArticlesChannelDescription"

@SuppressLint("UnspecifiedImmutableFlag")
class NewsNotificationHelper(private val context: Context) {

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setTicker(context.getString(R.string.new_articles_title))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.new_articles_title))
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
    }

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val contentIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            MainActivity.newIntent(context),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel())
        }
        return notificationBuilder.build()
    }

    fun updateNotification(notificationText: String? = null) {
        notificationText?.let { notificationBuilder.setContentText(it) }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() =
        NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
            setSound(null, null)
        }

    companion object {

        const val NOTIFICATION_ID = 99
    }
}