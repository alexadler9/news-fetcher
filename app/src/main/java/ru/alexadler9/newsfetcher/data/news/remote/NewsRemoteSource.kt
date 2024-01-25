package ru.alexadler9.newsfetcher.data.news.remote

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alexadler9.newsfetcher.data.news.remote.model.ArticlesRemoteModel
import ru.alexadler9.newsfetcher.data.news.remote.type.ArticlesCountryRemote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Source for accessing the remote news API.
 */
@Singleton
class NewsRemoteSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: NewsApi
) {

    /**
     * Get live top articles headlines.
     * @param country The country for which the articles will be searched.
     */
    suspend fun getTopHeadlinesArticles(country: ArticlesCountryRemote)
            : ArticlesRemoteModel {
        return api.getTopHeadlinesArticles(country = country)
    }

    /**
     * Load a wallpaper image for article via Glide.
     * @param url The image URL.
     * @param timeout Timeout for the http requests.
     */
    suspend fun getArticleWallpaper(url: String, timeout: Int = 5000): Bitmap {
        return withContext(Dispatchers.IO) {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .timeout(timeout)
                .submit()
                .get()
        }
    }
}