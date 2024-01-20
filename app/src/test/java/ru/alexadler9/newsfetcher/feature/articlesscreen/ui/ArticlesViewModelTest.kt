package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.articlesscreen.ArticlesInteractor
import ru.alexadler9.newsfetcher.utility.ARTICLE_MODEL_1
import ru.alexadler9.newsfetcher.utility.ARTICLE_MODEL_2
import ru.alexadler9.newsfetcher.utility.junit5.CoroutinesTestExtension
import ru.alexadler9.newsfetcher.utility.junit5.InstantExecutorExtension

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
    }

    /* runTest is a coroutine builder designed for testing. Use this to wrap any tests that include coroutines */
    @Test
    fun `successful loading of articles data`() = runTest {
        `when`(newsRepository.getArticleBookmarks()).thenReturn(
            flow {
                emit(listOf(ARTICLE_MODEL_1, ARTICLE_MODEL_2))
            }
        )
        `when`(newsRepository.getArticles()).thenReturn(listOf(ARTICLE_MODEL_1, ARTICLE_MODEL_2))

        subject = ArticlesViewModel(articlesInteractor)

        verify(newsRepository, times(1)).getArticles()
        verify(newsRepository, times(1)).getArticleBookmarks()
        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        assertThat(
            subject.viewState.value.state as State.Content, equalTo(
                State.Content(
                    listOf(
                        ArticleItem(ARTICLE_MODEL_1, true),
                        ArticleItem(ARTICLE_MODEL_2, true)
                    )
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
        `when`(newsRepository.getArticles()).thenReturn(listOf(ARTICLE_MODEL_1))
        `when`(newsRepository.articleBookmarkExist(anyString())).thenReturn(false)

        subject = ArticlesViewModel(articlesInteractor)

        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        assertThat(
            subject.viewState.value.state as State.Content, equalTo(
                State.Content(listOf(ArticleItem(ARTICLE_MODEL_1, false)))
            )
        )

        subject.processUiAction((UiAction.OnBookmarkButtonClicked(0)))

        // Fast forward virtual time
        testScheduler.advanceTimeBy(200)

        verify(newsRepository, times(1)).addArticleToBookmark(ARTICLE_MODEL_1)
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        assertThat(
            subject.viewState.value.state as State.Content, equalTo(
                State.Content(listOf(ArticleItem(ARTICLE_MODEL_1, true)))
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
        `when`(newsRepository.getArticles()).thenReturn(listOf(ARTICLE_MODEL_1))
        `when`(newsRepository.articleBookmarkExist(anyString())).thenReturn(true)

        subject = ArticlesViewModel(articlesInteractor)

        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        assertThat(
            subject.viewState.value.state as State.Content, equalTo(
                State.Content(listOf(ArticleItem(ARTICLE_MODEL_1, true)))
            )
        )

        subject.processUiAction((UiAction.OnBookmarkButtonClicked(0)))

        // Fast forward virtual time
        testScheduler.advanceTimeBy(200)

        verify(newsRepository, times(1)).deleteArticleFromBookmarks(ARTICLE_MODEL_1)
        assertThat(subject.viewState.value.state is State.Content, equalTo(true))
        assertThat(
            subject.viewState.value.state as State.Content, equalTo(
                State.Content(listOf(ArticleItem(ARTICLE_MODEL_1, false)))
            )
        )
    }

    @Test
    fun `articles data load failed`() = runTest {
        val exception = RuntimeException("Failed to load")

        `when`(newsRepository.getArticleBookmarks()).thenReturn(flow {})
        `when`(newsRepository.getArticles()).thenThrow(exception)

        subject = ArticlesViewModel(articlesInteractor)

        verify(newsRepository, times(1)).getArticles()
        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value.state is State.Error, equalTo(true))
        assertThat(subject.viewState.value.state as State.Error, equalTo(State.Error(exception)))
    }
}