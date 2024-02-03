package ru.alexadler9.newsfetcher.feature.detailsscreen.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.databinding.FragmentDetailsBinding
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import javax.inject.Inject
import kotlin.math.abs

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
        val article = DetailsFragmentArgs.fromBundle(requireArguments()).article
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

        with(binding) {
            ablDetails.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val percent = (abs(appBarLayout.totalScrollRange + verticalOffset).toFloat() /
                        appBarLayout.totalScrollRange)

                // Change scrim transparency.
                if ((appBarLayout.totalScrollRange + verticalOffset) < toolbarScrimBottom.height) {
                    val max =
                        (abs(toolbarScrimBottom.height).toFloat() / appBarLayout.totalScrollRange)
                    toolbarScrimBottom.alpha = 1.0f - ((max - percent) / max)
                } else {
                    toolbarScrimBottom.alpha = 1.0f
                }

                // Change background corners radius.
                nsvContent.background?.let {
                    val background = it as GradientDrawable
                    background.mutate()

                    background.cornerRadius = resources
                        .getDimensionPixelOffset(R.dimen.nested_scroll_view_radius) * percent
                }
            }
        }

        viewModel.viewState
            .onEach(::render)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(viewState: ViewState) {
        with(binding) {
            val article = viewState.article
            tvDetailsAuthor.text = article.author.ifBlank { "[Undefined]" }
            tvDetailsDate.text = article.publishedAtLocal
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
                    ivWallpaper.setImageBitmap(viewState.state.wallpaper)
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