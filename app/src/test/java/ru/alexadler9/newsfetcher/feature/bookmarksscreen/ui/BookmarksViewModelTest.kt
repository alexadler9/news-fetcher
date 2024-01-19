package ru.alexadler9.newsfetcher.feature.bookmarksscreen.ui

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
@OptIn(ExperimentalCoroutinesApi::class)
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
        Mockito.`when`(newsRepository.getArticleBookmarks()).thenReturn(
            flow {
                emit(listOf(ARTICLE_MODEL_1, ARTICLE_MODEL_2))
            }
        )

        // Start lazy initialization
        subject.viewState

        // Fast forward virtual time
        testScheduler.advanceTimeBy(200)

        Mockito.verify(newsRepository, Mockito.times(1)).getArticleBookmarks()
        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value?.state is State.Content, equalTo(true))
        assertThat(
            subject.viewState.value?.state as State.Content, equalTo(
                State.Content(
                    listOf(
                        ArticleItem(ARTICLE_MODEL_1, true),
                        ArticleItem(ARTICLE_MODEL_2, true)
                    ).reversed()
                )
            )
        )
    }

    @Test
    fun `successfully deleted an article from bookmarks`() = runTest {
        Mockito.`when`(newsRepository.getArticleBookmarks()).thenReturn(
            flow {
                emit(listOf(ARTICLE_MODEL_1))
                // Wait for the delete request and return an empty list
                delay(100)
                emit(emptyList())
            }
        )

        // Start lazy initialization
        subject.viewState

        // Fast forward virtual time
        testScheduler.advanceTimeBy(200)

        subject.processUiEvent(UiEvent.OnBookmarkButtonClicked(0))

        testScheduler.advanceTimeBy(200)

        Mockito.verify(newsRepository, Mockito.times(1))
            .deleteArticleFromBookmarks(anyExt(ArticleModel::class.java))
        assertThat(subject.viewState.value, notNullValue())
        assertThat(subject.viewState.value?.state is State.Content, equalTo(true))
        assertThat(
            subject.viewState.value?.state as State.Content,
            equalTo(State.Content(emptyList()))
        )
    }
}