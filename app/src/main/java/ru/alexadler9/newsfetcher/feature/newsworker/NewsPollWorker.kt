package ru.alexadler9.newsfetcher.feature.newsworker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.alexadler9.newsfetcher.MainActivity
import ru.alexadler9.newsfetcher.NOTIFICATION_CHANNEL_ID
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.data.news.NewsRepository

private const val TAG = "NEWS_POLL_WORKER"

@HiltWorker
class NewsPollWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: NewsRepository
) : CoroutineWorker(context, workerParams) {

    @SuppressLint("UnspecifiedImmutableFlag")
    override suspend fun doWork(): Result {
        val articles = repository.getTopHeadlinesArticles(
            country = repository.getArticlesCountry(),
            category = repository.getArticlesCategory(),
            query = ""
        )

        if (articles.isEmpty()) {
            return Result.success()
        }

        val articleUrl = articles.first().url
        if (articleUrl == repository.getLastArticleUrl()) {
            Log.i(TAG, "Got an old URL: $articleUrl")
        } else {
            Log.i(TAG, "Got a new URL: $articleUrl")

            repository.saveLastArticleUrl(articleUrl)

            val intent = MainActivity.newIntent(context)
            val pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val resources = context.resources
            val notification = NotificationCompat
                .Builder(context, NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.new_articles_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_articles_title))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            showBackgroundNotification(0, notification)
        }

        return Result.success()
    }

    private fun showBackgroundNotification(
        requestCode: Int,
        notification: Notification
    ) {
        val intent = Intent(ACTION_SHOW_NEWS_NOTIFICATION).apply {
            putExtra(EXTRA_REQUEST_CODE, requestCode)
            putExtra(EXTRA_NOTIFICATION, notification)
        }
        context.sendOrderedBroadcast(intent, PERM_PRIVATE_NEWS_NOTIFICATIONS)
    }

    companion object {
        const val ACTION_SHOW_NEWS_NOTIFICATION =
            "ru.alexadler9.newsfetcher.SHOW_NEWS_NOTIFICATION"

        const val PERM_PRIVATE_NEWS_NOTIFICATIONS =
            "ru.alexadler9.newsfetcher.PRIVATE_NEWS_NOTIFICATIONS"

        const val EXTRA_REQUEST_CODE = "REQUEST_CODE"
        const val EXTRA_NOTIFICATION = "NOTIFICATION"
    }
}