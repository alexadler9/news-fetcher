package ru.alexadler9.newsfetcher

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alexadler9.newsfetcher.data.NewsRepositoryImpl
import ru.alexadler9.newsfetcher.data.remote.NewsApi
import ru.alexadler9.newsfetcher.data.remote.NewsRemoteSource

class MainActivity : AppCompatActivity() {

    private val newsRepository by lazy {
        NewsRepositoryImpl(NewsRemoteSource(NewsApi.getInstance()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            val result = newsRepository.getArticles()
            Log.i("NEWS-API", result.toString())
        }
    }
}