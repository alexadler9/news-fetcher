package ru.alexadler9.newsfetcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import ru.alexadler9.newsfetcher.databinding.ActivityMainBinding
import ru.alexadler9.newsfetcher.feature.articlesscreen.ui.ArticlesFragment
import ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui.BookmarksFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            bottomNav.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.articlesFragment -> {
                        selectTab(ArticlesFragment())
                    }
                    R.id.bookmarksFragment -> {
                        selectTab(BookmarksFragment())
                    }
                    else -> {}
                }
                true
            }
        }
    }

    private fun selectTab(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, fragment)
        }
    }
}