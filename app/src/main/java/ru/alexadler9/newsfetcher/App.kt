package ru.alexadler9.newsfetcher

import android.app.Application
import ru.alexadler9.newsfetcher.data.remote.NewsApi

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NewsApi.getInstance()
    }
}