package ru.alexadler9.newsfetcher

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import ru.alexadler9.newsfetcher.data.NewsRepositoryImpl
import ru.alexadler9.newsfetcher.data.remote.NewsApi
import ru.alexadler9.newsfetcher.data.remote.NewsRemoteSource
import ru.alexadler9.newsfetcher.domain.NewsInteractor
import ru.alexadler9.newsfetcher.ui.ArticlesScreenViewModel
import ru.alexadler9.newsfetcher.ui.ArticlesScreenViewModelFactory
import ru.alexadler9.newsfetcher.ui.State
import ru.alexadler9.newsfetcher.ui.ViewState

class MainActivity : AppCompatActivity() {

    private val tvDebug by lazy { findViewById<TextView>(R.id.tvDebug) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModelFactory = ArticlesScreenViewModelFactory(NewsInteractor(
            NewsRepositoryImpl(
                NewsRemoteSource(NewsApi.getInstance())
            )
        ))
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ArticlesScreenViewModel::class.java)
        viewModel.viewState.observe(this, ::render)
    }

    private fun render(viewState: ViewState) {
        when (viewState.state) {
            is State.Load -> {
                tvDebug.isVisible = false
            }

            is State.Content -> {
                tvDebug.isVisible = true
                tvDebug.text = viewState.state.articles.toString()
            }

            is State.Error -> {
                tvDebug.isVisible = true
                tvDebug.text = "Error: ${viewState.state.throwable.message}"
            }
        }
    }
}