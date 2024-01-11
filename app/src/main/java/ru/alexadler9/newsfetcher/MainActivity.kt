package ru.alexadler9.newsfetcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.alexadler9.newsfetcher.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        with(binding) {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNav.isVisible =
                    (destination.id == R.id.articlesFragment || destination.id == R.id.bookmarksFragment)
            }
            bottomNav.setupWithNavController(navController)
        }
    }
}