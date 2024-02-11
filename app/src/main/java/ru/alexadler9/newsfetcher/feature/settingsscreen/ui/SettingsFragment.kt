package ru.alexadler9.newsfetcher.feature.settingsscreen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.alexadler9.newsfetcher.base.ext.serializable
import ru.alexadler9.newsfetcher.databinding.FragmentSettingsBinding
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry
import ru.alexadler9.newsfetcher.feature.settingsscreen.ui.dialog.CategoryPickerFragment
import ru.alexadler9.newsfetcher.feature.settingsscreen.ui.dialog.CountryPickerFragment

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCountry.setOnClickListener {
            SettingsFragmentDirections.actionSettingsFragmentToCountryPickerFragment(
                viewModel.viewState.value.country
            ).apply {
                findNavController().navigate(this)
            }
        }
        setFragmentResultListener(CountryPickerFragment.REQUEST_COUNTRY) { _, bundle ->
            bundle.serializable<ArticlesCountry>(CountryPickerFragment.ARG_COUNTRY)
                ?.let { country ->
                    viewModel.processUiAction(UiAction.OnCountryChanged(country))
                }
        }

        binding.btnCategory.setOnClickListener {
            SettingsFragmentDirections.actionSettingsFragmentToCategoryPickerFragment(
                viewModel.viewState.value.category
            ).apply {
                findNavController().navigate(this)
            }
        }
        setFragmentResultListener(CategoryPickerFragment.REQUEST_CATEGORY) { _, bundle ->
            bundle.serializable<ArticlesCategory>(CategoryPickerFragment.ARG_CATEGORY)
                ?.let { category ->
                    viewModel.processUiAction(UiAction.OnCategoryChanged(category))
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
            tvSelectedCountry.text = viewState.country.title
            tvSelectedCategory.text = viewState.category.title
        }
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