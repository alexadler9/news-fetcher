package ru.alexadler9.newsfetcher.feature.detailsscreen

import android.graphics.Bitmap
import ru.alexadler9.newsfetcher.base.Either
import ru.alexadler9.newsfetcher.base.ext.attempt
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import javax.inject.Inject

/**
 * Article details interactor.
 */
class ArticleDetailsInteractor @Inject constructor(private val repository: NewsRepository) {

    /**
     * Get a wallpaper image for article.
     * @param article The article.
     */
    suspend fun getArticleWallpaper(article: ArticleModel): Either<Throwable, Bitmap> {
        return attempt { repository.getArticleWallpaper(article) }
    }
}