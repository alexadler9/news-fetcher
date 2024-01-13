package ru.alexadler9.newsfetcher.feature.articlesscreen.ui

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.articlesscreen.ArticlesInteractor
import ru.alexadler9.newsfetcher.utility.ARTICLE_MODEL_1
import ru.alexadler9.newsfetcher.utility.ARTICLE_MODEL_2
import ru.alexadler9.newsfetcher.utility.ext.CoroutinesTestExtension
import ru.alexadler9.newsfetcher.utility.ext.InstantExecutorExtension

@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class ArticlesViewModelTest {

    private lateinit var newsRepository: NewsRepository
    private lateinit var articlesInteractor: ArticlesInteractor
    private lateinit var subject: ArticlesViewModel

    @BeforeEach
    fun setUp() {
        newsRepository = mock(NewsRepository::class.java)
        articlesInteractor = ArticlesInteractor(newsRepository)
        subject = ArticlesViewModel(articlesInteractor)
    }

    /* runTest is a coroutine builder designed for testing. Use this to wrap any tests that include coroutines */
    @Test
    fun `successful loading of articles data`() = runTest {
        `when`(newsRepository.getArticles()).thenReturn(listOf(ARTICLE_MODEL_1, ARTICLE_MODEL_2))
        `when`(newsRepository.articleBookmarkExist(anyString())).thenReturn(true)

        subject.processUiEvent(UiEvent.OnViewCreated)

        verify(newsRepository, times(1)).getArticles()
        verify(newsRepository, times(2)).articleBookmarkExist(anyString())
        assertEquals(subject.viewState.value?.state is State.Content, true)
        assertEquals(
            subject.viewState.value?.state as State.Content, State.Content(
                listOf(
                    ArticleItem(ARTICLE_MODEL_1, true),
                    ArticleItem(ARTICLE_MODEL_2, true)
                )
            )
        )
    }

    @Test
    fun `successfully added an article to bookmarks`() = runTest {
        `when`(newsRepository.getArticles()).thenReturn(listOf(ARTICLE_MODEL_1))
        `when`(newsRepository.articleBookmarkExist(anyString())).thenReturn(false)

        subject.processUiEvent(UiEvent.OnViewCreated)

        assertEquals(
            subject.viewState.value?.state as State.Content, State.Content(
                listOf(
                    ArticleItem(ARTICLE_MODEL_1, false)
                )
            )
        )

        subject.processUiEvent((UiEvent.OnBookmarkButtonClicked(0)))

        verify(newsRepository, times(1)).addArticleToBookmark(ARTICLE_MODEL_1)
        assertEquals(
            subject.viewState.value?.state as State.Content, State.Content(
                listOf(
                    ArticleItem(ARTICLE_MODEL_1, true)
                )
            )
        )
    }

    @Test
    fun `successfully deleted an article from bookmarks`() = runTest {
        `when`(newsRepository.getArticles()).thenReturn(listOf(ARTICLE_MODEL_1))
        `when`(newsRepository.articleBookmarkExist(anyString())).thenReturn(true)

        subject.processUiEvent(UiEvent.OnViewCreated)

        assertEquals(
            subject.viewState.value?.state as State.Content, State.Content(
                listOf(
                    ArticleItem(ARTICLE_MODEL_1, true)
                )
            )
        )

        subject.processUiEvent((UiEvent.OnBookmarkButtonClicked(0)))

        verify(newsRepository, times(1)).deleteArticleFromBookmarks(ARTICLE_MODEL_1)
        assertEquals(
            subject.viewState.value?.state as State.Content, State.Content(
                listOf(
                    ArticleItem(ARTICLE_MODEL_1, false)
                )
            )
        )
    }

    @Test
    fun `articles data load failed`() = runTest {
        val exception = RuntimeException("Failed to load")

        `when`(newsRepository.getArticles()).thenThrow(exception)

        subject.processUiEvent(UiEvent.OnViewCreated)

        verify(newsRepository, times(1)).getArticles()
        verify(newsRepository, times(0)).articleBookmarkExist(anyString())
        assertEquals(subject.viewState.value?.state is State.Error, true)
        assertEquals(subject.viewState.value?.state as State.Error, State.Error(exception))
    }
}