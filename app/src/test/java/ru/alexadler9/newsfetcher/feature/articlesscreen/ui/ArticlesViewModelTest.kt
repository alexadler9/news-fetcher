package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.domain.type.ArticlesCategory
import ru.alexadler9.newsfetcher.domain.type.ArticlesCountry
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.articlesscreen.ArticlesInteractor
import ru.alexadler9.newsfetcher.utility.*
import ru.alexadler9.newsfetcher.utility.junit5.CoroutinesTestExtension
import ru.alexadler9.newsfetcher.utility.junit5.InstantExecutorExtension
import ru.alexadler9.newsfetcher.utility.mockito.anyExt

@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesViewModelTest {

    private lateinit var newsRepository: NewsRepository
    private lateinit var articlesInteractor: ArticlesInteractor
    private lateinit var subject: ArticlesViewModel

    @BeforeEach
    fun setUp() {
        newsRepository = mock(NewsRepository::class.java)
        articlesInteractor = ArticlesInteractor(newsRepository)
//        subject = ArticlesViewModel(articlesInteractor)

        `when`(newsRepository.getArticlesCountry()).thenReturn(
            ArticlesCountry.RUSSIA
        )
        `when`(newsRepository.getArticlesCategory()).thenReturn(
            ArticlesCategory.GENERAL
        )
    }

    /* runTest is a coroutine builder designed for testing. Use this to wrap any tests that include coroutines */
    @Test
    fun `successful loading of articles data`() = runTest {
        `when`(newsRepository.getArticleBookmarks()).thenReturn(
            flow {
                emit(listOf(ARTICLE_MODEL_1, ARTICLE_MODEL_2))
            }
        )
        `when`(
            newsRepository.getTopHeadlinesArticlesPagingSource(
                anyExt(ArticlesCountry::class.java),
                anyExt(ArticlesCategory::class.java)
            )
        ).thenReturn(
            listOf(ARTICLE_MODEL_1, ARTICLE_MODEL_2)
                .asPagingSourceFactory()
                .invoke()
        )

        subject = ArticlesViewModel(articlesInteractor)
        subject.processUiAction(UiAction.OnPagerStateChanged(PAGER_STATES_LOADED))

        verify(newsRepository, times(1))
            .getTopHeadlinesArticlesPagingSource(
                anyExt(ArticlesCountry::class.java),
                anyExt(ArticlesCategory::class.java)
            )
        verify(newsRepository, times(1)).getArticleBookmarks()
        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        val articles = flowOf(subject.viewState.value.articlesPagingData).asSnapshot()
        assertThat(
            articles, equalTo(
                listOf(
                    ArticleItem(ARTICLE_MODEL_1, true),
                    ArticleItem(ARTICLE_MODEL_2, true)
                )
            )
        )
    }

    @Test
    fun `successfully added an article to bookmarks`() = runTest {
        `when`(newsRepository.getArticleBookmarks()).thenReturn(
            flow {
                emit(emptyList())
                // Wait for the add request and return bookmarks
                delay(100)
                emit(listOf(ARTICLE_MODEL_1))
            }
        )
        `when`(
            newsRepository.getTopHeadlinesArticlesPagingSource(
                anyExt(ArticlesCountry::class.java),
                anyExt(ArticlesCategory::class.java)
            )
        ).thenReturn(
            listOf(ARTICLE_MODEL_1)
                .asPagingSourceFactory()
                .invoke()
        )
        `when`(newsRepository.articleBookmarkExist(anyString())).thenReturn(false)

        subject = ArticlesViewModel(articlesInteractor)
        subject.processUiAction(UiAction.OnPagerStateChanged(PAGER_STATES_LOADED))

        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        var articles = flowOf(subject.viewState.value.articlesPagingData).asSnapshot()
        assertThat(
            articles, equalTo(
                listOf(ArticleItem(ARTICLE_MODEL_1, false))
            )
        )

        subject.processUiAction(
            UiAction.OnBookmarkButtonClicked(
                ArticleItem(ARTICLE_MODEL_1, false)
            )
        )

        // Fast forward virtual time
        testScheduler.advanceTimeBy(200)

        verify(newsRepository, times(1)).addArticleToBookmark(ARTICLE_MODEL_1)
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        articles = flowOf(subject.viewState.value.articlesPagingData).asSnapshot()
        assertThat(
            articles, equalTo(
                listOf(ArticleItem(ARTICLE_MODEL_1, true))
            )
        )
    }

    @Test
    fun `successfully deleted an article from bookmarks`() = runTest {
        `when`(newsRepository.getArticleBookmarks()).thenReturn(
            flow {
                emit(listOf(ARTICLE_MODEL_1))
                // Wait for the delete request and return an empty list
                delay(100)
                emit(emptyList())
            }
        )
        `when`(
            newsRepository.getTopHeadlinesArticlesPagingSource(
                anyExt(ArticlesCountry::class.java),
                anyExt(ArticlesCategory::class.java)
            )
        ).thenReturn(
            listOf(ARTICLE_MODEL_1)
                .asPagingSourceFactory()
                .invoke()
        )
        `when`(newsRepository.articleBookmarkExist(anyString())).thenReturn(true)

        subject = ArticlesViewModel(articlesInteractor)
        subject.processUiAction(UiAction.OnPagerStateChanged(PAGER_STATES_LOADED))

        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        var articles = flowOf(subject.viewState.value.articlesPagingData).asSnapshot()
        assertThat(
            articles, equalTo(
                listOf(ArticleItem(ARTICLE_MODEL_1, true))
            )
        )

        subject.processUiAction(
            UiAction.OnBookmarkButtonClicked(
                ArticleItem(ARTICLE_MODEL_1, true)
            )
        )

        // Fast forward virtual time
        testScheduler.advanceTimeBy(200)

        verify(newsRepository, times(1)).deleteArticleFromBookmarks(ARTICLE_MODEL_1)
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        articles = flowOf(subject.viewState.value.articlesPagingData).asSnapshot()
        assertThat(
            articles, equalTo(
                listOf(ArticleItem(ARTICLE_MODEL_1, false))
            )
        )
    }

    @Test
    fun `articles data load failed`() = runTest {
        `when`(newsRepository.getArticleBookmarks()).thenReturn(flow {})
        `when`(
            newsRepository.getTopHeadlinesArticlesPagingSource(
                anyExt(ArticlesCountry::class.java),
                anyExt(ArticlesCategory::class.java)
            )
        ).thenReturn(
            emptyList<ArticleModel>()
                .asPagingSourceFactory()
                .invoke()
        )

        subject = ArticlesViewModel(articlesInteractor)
        subject.processUiAction((UiAction.OnPagerStateChanged(PAGER_STATES_ERROR)))

        verify(newsRepository, times(1))
            .getTopHeadlinesArticlesPagingSource(
                anyExt(ArticlesCountry::class.java),
                anyExt(ArticlesCategory::class.java)
            )
        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value.state is State.Error, equalTo(true))
        assertThat(
            subject.viewState.value.state as State.Error,
            equalTo(State.Error(EXCEPTION_LOAD))
        )
    }
}