package ru.alexadler9.newsfetcher

import android.app.Application
import ru.alexadler9.newsfetcher.di.AppComponent
import ru.alexadler9.newsfetcher.di.DaggerAppComponent

class App : Application() {

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        initializeComponent()
    }

    override fun onCreate() {
        super.onCreate()
    }

    private fun initializeComponent(): AppComponent {
        return DaggerAppComponent.create()
    }
}