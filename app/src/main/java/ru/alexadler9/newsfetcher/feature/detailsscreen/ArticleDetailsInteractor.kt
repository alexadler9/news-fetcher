package ru.alexadler9.newsfetcher.feature.detailsscreen

import android.content.Context
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

    /**
     * Share article in messengers.
     * The message will be generated from the title and URL of the article.
     * @param context The context.
     * @param article The article.
     */
    fun shareArticle(context: Context, article: ArticleModel) {
        repository.shareArticle(context, article)
    }

    /**
     * Open original article in browser.
     * @param context The context.
     * @param article The article.
     */
    fun openArticleInBrowser(context: Context, article: ArticleModel) {
        repository.openArticleInBrowser(context, article)
    }
}