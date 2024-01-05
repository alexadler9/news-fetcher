package ru.alexadler9.newsfetcher.di

import dagger.Module
import ru.alexadler9.newsfetcher.feature.articlesscreen.di.ArticlesComponent

// This module tells AppComponent which are its subcomponents
@Module(subcomponents = [ArticlesComponent::class])
class AppSubcomponents