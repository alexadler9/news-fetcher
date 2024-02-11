package ru.alexadler9.newsfetcher.feature.settingsscreen.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import ru.alexadler9.newsfetcher.R
import ru.alexadler9.newsfetcher.base.ext.toPx
import ru.alexadler9.newsfetcher.databinding.FragmentPickerBinding
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry


/**
 * Fragment is responsible for choosing the country.
 * Use the [CountryPickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CountryPickerFragment : DialogFragment() {

    private var _binding: FragmentPickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentPickerBinding.inflate(LayoutInflater.from(context))

        val countrySelected = CountryPickerFragmentArgs.fromBundle(requireArguments()).country
        ArticlesCountry.values().forEach { country ->
            RadioButton(requireContext()).apply {
                id = View.generateViewId()
                text = country.title
                isChecked = (country.title == countrySelected.title)
                setPadding(16.toPx, 0, 0, 0)
                layoutParams = RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 4.toPx, 0, 4.toPx)
                }
                setOnClickListener {
                    val rb = it as RadioButton
                    setFragmentResult(
                        REQUEST_COUNTRY,
                        Bundle().apply {
                            putSerializable(
                                ARG_COUNTRY,
                                ArticlesCountry.forTitle(rb.text.toString())
                            )
                        }
                    )
                    this@CountryPickerFragment.dismiss()
                }
                binding.rgSettings.addView(this)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.picker_country_title)
            .setView(binding.root)
            .setNegativeButton(R.string.picker_button_negative) { _, _ ->
            }
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val ARG_COUNTRY = "ARG_COUNTRY"
        const val REQUEST_COUNTRY = "CountryPickerFragment_REQUEST_COUNTRY"

        /**
         * Create a new instance of this fragment.
         * @param country Selected country.
         * @return A new instance of fragment CountryPickerFragment.
         */
        fun newInstance(country: ArticlesCountry): CountryPickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_COUNTRY, country)
            }
            return CountryPickerFragment().apply {
                arguments = args
            }
        }
    }
}