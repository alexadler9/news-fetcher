package ru.alexadler9.newsfetcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.alexadler9.newsfetcher.domain.NewsInteractor

class ArticlesScreenViewModelFactory(private val interactor: NewsInteractor)
    : ViewModelProvider.Factory {

        override fun<T: ViewModel> create(modelClass: Class<T>) : T {
            if (modelClass.isAssignableFrom(ArticlesScreenViewModel::class.java)) {
                return ArticlesScreenViewModel(interactor) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
}
