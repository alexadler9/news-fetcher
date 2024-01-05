package ru.alexadler9.newsfetcher.feature.articlesscreen.di

import dagger.Subcomponent
import ru.alexadler9.newsfetcher.feature.articlesscreen.ui.ArticlesFragment

@Subcomponent
interface ArticlesComponent {

    // Factory to create instances of ArticlesComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): ArticlesComponent
    }

    fun inject(articlesFragment: ArticlesFragment)
    // or:
    // fun getSomething() : Something
}