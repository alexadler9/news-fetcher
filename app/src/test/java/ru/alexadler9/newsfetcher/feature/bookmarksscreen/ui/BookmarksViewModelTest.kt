package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import ru.alexadler9.newsfetcher.data.news.NewsRepository
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import ru.alexadler9.newsfetcher.feature.adapter.ArticleItem
import ru.alexadler9.newsfetcher.feature.bookmarksscreen.BookmarksInteractor
import ru.alexadler9.newsfetcher.utility.ARTICLE_MODEL_1
import ru.alexadler9.newsfetcher.utility.ARTICLE_MODEL_2
import ru.alexadler9.newsfetcher.utility.ext.anyExt
import ru.alexadler9.newsfetcher.utility.junit5.CoroutinesTestExtension
import ru.alexadler9.newsfetcher.utility.junit5.InstantExecutorExtension

@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
internal class BookmarksViewModelTest {

    private lateinit var newsRepository: NewsRepository
    private lateinit var bookmarksInteractor: BookmarksInteractor
    private lateinit var subject: BookmarksViewModel

    @BeforeEach
    fun setUp() {
        newsRepository = Mockito.mock(NewsRepository::class.java)
        bookmarksInteractor = BookmarksInteractor(newsRepository)
        subject = BookmarksViewModel(bookmarksInteractor)
    }

    /* runTest is a coroutine builder designed for testing. Use this to wrap any tests that include coroutines */
    @Test
    fun `successful loading of bookmarks data`() = runTest {
        Mockito.`when`(newsRepository.getArticleBookmarks())
            .thenReturn(listOf(ARTICLE_MODEL_1, ARTICLE_MODEL_2))

        subject.processUiEvent(UiEvent.OnViewCreated)

        Mockito.verify(newsRepository, Mockito.times(1)).getArticleBookmarks()
        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value?.state is State.Content, equalTo(true))
        assertThat(
            subject.viewState.value?.state as State.Content, equalTo(
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
    fun `successfully deleted an article from bookmarks`() = runTest {
        Mockito.`when`(newsRepository.getArticleBookmarks()).thenReturn(listOf(ARTICLE_MODEL_1))

        subject.processUiEvent(UiEvent.OnViewCreated)
        subject.processUiEvent((UiEvent.OnBookmarkButtonClicked(0)))

        Mockito.verify(newsRepository, Mockito.times(1))
            .deleteArticleFromBookmarks(anyExt(ArticleModel::class.java))
        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value?.state is State.Content, equalTo(true))
        assertThat(
            subject.viewState.value?.state as State.Content,
            equalTo(State.Content(emptyList()))
        )
    }

    @Test
    fun `bookmarks data load failed`() = runTest {
        val exception = RuntimeException("Failed to load")

        Mockito.`when`(newsRepository.getArticleBookmarks()).thenThrow(exception)

        subject.processUiEvent(UiEvent.OnViewCreated)

        Mockito.verify(newsRepository, Mockito.times(1)).getArticleBookmarks()
        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value?.state is State.Error, equalTo(true))
        assertThat(subject.viewState.value?.state as State.Error, equalTo(State.Error(exception)))
    }
}