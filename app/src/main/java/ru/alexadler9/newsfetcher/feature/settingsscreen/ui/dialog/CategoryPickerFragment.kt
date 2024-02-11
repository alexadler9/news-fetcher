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
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory

/**
 * Fragment is responsible for choosing the category.
 * Use the [CategoryPickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryPickerFragment : DialogFragment() {

    private var _binding: FragmentPickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentPickerBinding.inflate(LayoutInflater.from(context))

        val categorySelected = CategoryPickerFragmentArgs.fromBundle(requireArguments()).category
        ArticlesCategory.values().forEach { category ->
            RadioButton(requireContext()).apply {
                id = View.generateViewId()
                text = category.title
                isChecked = (category.title == categorySelected.title)
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
                        REQUEST_CATEGORY,
                        Bundle().apply {
                            putSerializable(
                                ARG_CATEGORY,
                                ArticlesCategory.forTitle(rb.text.toString())
                            )
                        }
                    )
                    this@CategoryPickerFragment.dismiss()
                }
                binding.rgSettings.addView(this)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.picker_category_title)
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

        const val ARG_CATEGORY = "ARG_CATEGORY"
        const val REQUEST_CATEGORY = "CategoryPickerFragment_REQUEST_CATEGORY"

        /**
         * Create a new instance of this fragment.
         * @param category Selected category.
         * @return A new instance of fragment CategoryPickerFragment.
         */
        fun newInstance(category: ArticlesCategory): CategoryPickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CATEGORY, category)
            }
            return CategoryPickerFragment().apply {
                arguments = args
            }
        }
    }
}