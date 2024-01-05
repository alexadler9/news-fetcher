package ru.alexadler9.newsfetcher.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.alexadler9.newsfetcher.feature.articlesscreen.domain.ArticlesInteractor
import ru.alexadler9.newsfetcher.feature.articlesscreen.ui.ArticlesViewModel
import kotlin.reflect.KClass

@Module
class ViewModelModule {

    @MapKey
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @IntoMap
    @ViewModelKey(ArticlesViewModel::class)
    @Provides
    fun provideArticlesScreenViewModel(interactor: ArticlesInteractor): ViewModel {
        return ArticlesViewModel(interactor)
    }
}