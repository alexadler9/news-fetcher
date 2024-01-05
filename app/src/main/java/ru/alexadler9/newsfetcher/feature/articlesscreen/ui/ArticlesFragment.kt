package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.alexadler9.newsfetcher.R

@AndroidEntryPoint
class ArticlesFragment : Fragment() {

    private val tvDebug by lazy { requireActivity().findViewById<TextView>(R.id.tvDebug) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_articles, container, false)

        val viewModel = ViewModelProvider(requireActivity()).get(ArticlesViewModel::class.java)
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