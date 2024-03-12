package ru.alexadler9.newsfetcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import ru.alexadler9.newsfetcher.databinding.ActivityMainBinding
import ru.alexadler9.newsfetcher.feature.newsworker.NewsPollWorker

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
        val workRequest = OneTimeWorkRequestBuilder<NewsPollWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}