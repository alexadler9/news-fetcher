package ru.alexadler9.newsfetcher.feature.detailsscreen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.scale
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.base.ext.serializable
import ru.alexadler9.newsfetcher.databinding.FragmentDetailsBinding
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import javax.inject.Inject

/**
 * Fragment is responsible for for displaying the article details.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var detailsViewModelFactory: DetailsViewModel.DetailsViewModelFactory

    private val viewModel: DetailsViewModel by viewModels {
        val article = this.arguments?.serializable<ArticleModel>(ARG_ARTICLE) ?: ArticleModel()
        DetailsViewModel.provideFactory(detailsViewModelFactory, article)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner, ::render)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(viewState: ViewState) {
        with(binding) {
            val article = viewState.article
            tvDetailsAuthor.text = article.author.ifBlank { "[Undefined]" }
            tvDetailsDate.text = article.publishedAt
            tvDescription.text = article.description
            ctlDetails.title = article.title
            when (viewState.state) {
                is State.Load -> {
                    pbDetails.isVisible = true
                    ablDetails.isVisible = false
                    nsvContent.isVisible = false
                }

                is State.Content -> {
                    pbDetails.isVisible = false
                    ablDetails.isVisible = true
                    nsvContent.isVisible = true
                    ivWallpaper.setImageBitmap(
                        viewState.state.wallpaper.scale(
                            ivWallpaper.width,
                            ivWallpaper.height
                        )
                    )
                }

                is State.Error -> {
                    pbDetails.isVisible = false
                    ablDetails.isVisible = true
                    nsvContent.isVisible = true
                    ivWallpaper.setImageResource(R.drawable.img_wallpaper)
                }
            }
        }
    }

    companion object {
        private const val ARG_ARTICLE = "ARG_ARTICLE"

        /**
         * Create a new instance of this fragment.
         *
         * @param article The article.
         * @return A new instance of fragment DetailsFragment.
         */
        fun newInstance(article: ArticleModel) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ARTICLE, article)
                }
            }
    }
}