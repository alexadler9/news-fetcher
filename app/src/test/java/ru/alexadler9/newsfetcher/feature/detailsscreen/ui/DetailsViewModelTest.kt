package ru.alexadler9.newsfetcher.feature.detailsscreen.ui

import android.R
import android.app.Application
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.feature.detailsscreen.ArticleDetailsInteractor
import ru.alexadler9.newsfetcher.utility.ARTICLE_MODEL_1
import ru.alexadler9.newsfetcher.utility.ext.anyExt

/**
 * JUnit4 (junit-vintage-engine) + Robolectric
 */
@RunWith(RobolectricTestRunner::class)
class DetailsViewModelTest {

    private lateinit var newsRepository: NewsRepository
    private lateinit var articleDetailsInteractor: ArticleDetailsInteractor
    private lateinit var subject: DetailsViewModel

    @Before
    fun setUp() {
        newsRepository = Mockito.mock(NewsRepository::class.java)
        articleDetailsInteractor = ArticleDetailsInteractor(newsRepository)
        subject = DetailsViewModel(articleDetailsInteractor, ARTICLE_MODEL_1)
    }

    @Test
    fun loadingWallpaper_successful() = runTest {
        val bitmap = BitmapFactory.decodeResource(
            ApplicationProvider.getApplicationContext<Application>().resources,
            R.drawable.ic_lock_power_off
        )

        Mockito.`when`(newsRepository.getArticleWallpaper(anyExt(ArticleModel::class.java)))
            .thenReturn(bitmap)

        subject.processUiEvent(UiEvent.OnViewCreated)

        Mockito.verify(newsRepository, Mockito.times(1))
            .getArticleWallpaper(anyExt(ArticleModel::class.java))

        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value?.article, equalTo(ARTICLE_MODEL_1))
        assertThat(subject.viewState.value?.state is State.Content, equalTo(true))
        assertThat(subject.viewState.value?.state as State.Content, equalTo(State.Content(bitmap)))
    }

    @Test
    fun loadingWallpaper_unsuccessful() = runTest {
        val exception = RuntimeException("Failed to load")

        Mockito.`when`(newsRepository.getArticleWallpaper(anyExt(ArticleModel::class.java)))
            .thenThrow(exception)

        subject.processUiEvent(UiEvent.OnViewCreated)

        Mockito.verify(newsRepository, Mockito.times(1))
            .getArticleWallpaper(anyExt(ArticleModel::class.java))

        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value?.article, equalTo(ARTICLE_MODEL_1))
        assertThat(subject.viewState.value?.state is State.Error, equalTo(true))
        assertThat(subject.viewState.value?.state as State.Error, equalTo(State.Error(exception)))
    }
}