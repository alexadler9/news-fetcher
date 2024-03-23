package ru.alexadler9.newsfetcher.feature.newsworker

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

/**
 * A worker for periodically checking for new articles on a web service.
 * Sends a notification if new articles are found.
 * Use the [NewsPollWorker.start] and [NewsPollWorker.stop] methods to control the worker.
 */
@HiltWorker
class NewsPollWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: NewsRepository
) : CoroutineWorker(context, workerParams) {

    private val notificationHelper by lazy { NewsNotificationHelper(context) }

    override suspend fun doWork(): Result {
        val articles = try {
            repository.getTopHeadlinesArticles(
                country = repository.getArticlesCountry(),
                category = repository.getArticlesCategory(),
                query = ""
            )
        } catch (e: Throwable) {
            return Result.success()
        }

        if (articles.isNotEmpty()) {
            val articleUrl = articles.first().url
            if (articleUrl == repository.getLastArticleUrl()) {
                Log.i(TAG, "Got an old URL: $articleUrl")
            } else {
                Log.i(TAG, "Got a new URL: $articleUrl")
                repository.saveLastArticleUrl(articleUrl)
                showBackgroundNotification(notificationHelper.getNotification())
            }
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

        /**
         * Queues the periodic work with the following parameters:
         * - starts with a period of 15 minutes;
         * - requires Internet access.
         * If the work is already running, the restart is ignored.
         */
        fun start(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
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

        /**
         * Stops the periodic work.
         */
        fun stop(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(NEWS_POLL_WORKER_NAME)
        }
    }
}