package ru.alexadler9.newsfetcher.di

import dagger.Component
import ru.alexadler9.newsfetcher.di.scopes.AppScope
import ru.alexadler9.newsfetcher.feature.articlesscreen.di.ArticlesComponent

@AppScope
@Component(modules = [NetworkModule::class, ViewModelModule::class, AppSubcomponents::class])
interface AppComponent {

    fun articlesComponent(): ArticlesComponent.Factory
}