package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import ru.alexadler9.newsfetcher.App
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.base.ViewModelFactory
import javax.inject.Inject

class ArticlesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val tvDebug by lazy { requireActivity().findViewById<TextView>(R.id.tvDebug) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_articles, container, false)

        val articlesScreenComponent =
            (requireActivity().application as App).appComponent.articlesComponent().create()
        articlesScreenComponent.inject(this)

        val viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(ArticlesViewModel::class.java)
        viewModel.viewState.observe(viewLifecycleOwner, ::render)

        return view
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