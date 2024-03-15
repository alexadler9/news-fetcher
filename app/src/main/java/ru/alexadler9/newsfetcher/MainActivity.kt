package ru.alexadler9.newsfetcher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import ru.alexadler9.newsfetcher.databinding.ActivityMainBinding
import ru.alexadler9.newsfetcher.feature.newsworker.NewsPollWorker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        with(binding) {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNav.isVisible = (destination.id != R.id.detailsFragment)
                toolbar.isVisible = (destination.id != R.id.detailsFragment)
            }
            bottomNav.setupWithNavController(navController)
        }

        // Test worker
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val periodicRequest = PeriodicWorkRequest
            .Builder(NewsPollWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "NEWS_POLL_WORK",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}