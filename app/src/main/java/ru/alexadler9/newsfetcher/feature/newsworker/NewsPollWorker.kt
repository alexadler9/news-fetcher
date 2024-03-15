package ru.alexadler9.newsfetcher.feature.newsworker

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import java.util.concurrent.TimeUnit

private const val TAG = "NEWS_POLL_WORKER"

@HiltWorker
class NewsPollWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: NewsRepository
) : CoroutineWorker(context, workerParams) {

    private val notificationHelper by lazy { NewsNotificationHelper(context) }

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
            showBackgroundNotification(notificationHelper.getNotification())
        }

        return Result.success()
    }

    private fun showBackgroundNotification(notification: Notification) {
        val intent = Intent(ACTION_SHOW_NEWS_NOTIFICATION).apply {
            putExtra(EXTRA_REQUEST_CODE, 0)
            putExtra(EXTRA_NOTIFICATION, notification)
        }
        context.sendOrderedBroadcast(intent, PERM_PRIVATE_NEWS_NOTIFICATIONS)
    }

    companion object {

        const val NEWS_POLL_WORKER_NAME =
            "ru.alexadler9.newsfetcher.NEWS_POLL_WORKER"

        const val ACTION_SHOW_NEWS_NOTIFICATION =
            "ru.alexadler9.newsfetcher.SHOW_NEWS_NOTIFICATION"

        const val PERM_PRIVATE_NEWS_NOTIFICATIONS =
            "ru.alexadler9.newsfetcher.PRIVATE_NEWS_NOTIFICATIONS"

        const val EXTRA_REQUEST_CODE = "REQUEST_CODE"
        const val EXTRA_NOTIFICATION = "NOTIFICATION"

        fun start(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            val periodicRequest = PeriodicWorkRequest
                .Builder(NewsPollWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                NEWS_POLL_WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
        }
    }
}