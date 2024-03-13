package ru.alexadler9.newsfetcher.feature.newsworker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
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
        }

        return Result.success()
    }
}