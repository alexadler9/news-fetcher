package ru.alexadler9.newsfetcher.feature.settingsscreen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.databinding.FragmentSettingsBinding
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry

/**
 * Fragment is responsible for setting parameters of the articles list.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    private val countriesAdapter: ArrayAdapter<ArticlesCountry> by lazy {
        ArrayAdapter(requireContext(), R.layout.item_spinner, ArticlesCountry.values()).also {
            it.setDropDownViewResource(R.layout.item_spinner)
        }
    }
    private val categoriesAdapter: ArrayAdapter<ArticlesCategory> by lazy {
        ArrayAdapter(requireContext(), R.layout.item_spinner, ArticlesCategory.values()).also {
            it.setDropDownViewResource(R.layout.item_spinner)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            spCountries.adapter = countriesAdapter
            spCategories.adapter = categoriesAdapter
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
    }

    companion object {
        /**
         * Create a new instance of this fragment.
         *
         * @return A new instance of fragment SettingsFragment.
         */
        fun newInstance() = SettingsFragment()
    }
}