package ru.alexadler9.newsfetcher.data.news

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import ru.alexadler9.newsfetcher.utility.ARTICLE_LOCAL_MODEL
import ru.alexadler9.newsfetcher.utility.ARTICLE_MODEL_1
import ru.alexadler9.newsfetcher.utility.ARTICLE_REMOTE_MODEL_1
import ru.alexadler9.newsfetcher.utility.ARTICLE_REMOTE_MODEL_EMPTY_FIELDS

class MapperKtTest {

    @Test
    fun `converts remote article model into a domain`() {
        val articleModel = ARTICLE_REMOTE_MODEL_1.toDomain()

        assertThat(articleModel.url, equalTo(ARTICLE_REMOTE_MODEL_1.url))
        assertThat(articleModel.author, equalTo(ARTICLE_REMOTE_MODEL_1.author))
        assertThat(articleModel.description, equalTo(ARTICLE_REMOTE_MODEL_1.description))
        assertThat(articleModel.title, equalTo(ARTICLE_REMOTE_MODEL_1.title))
        assertThat(articleModel.publishedAt, equalTo(ARTICLE_REMOTE_MODEL_1.publishedAt))
        assertThat(articleModel.urlToImage, equalTo(ARTICLE_REMOTE_MODEL_1.urlToImage))
    }

    @Test
    fun `converts remote article model with empty fields into a domain`() {
        val articleModel = ARTICLE_REMOTE_MODEL_EMPTY_FIELDS.toDomain()

        assertThat(articleModel.url, equalTo(ARTICLE_REMOTE_MODEL_EMPTY_FIELDS.url))
        assertThat(articleModel.author, equalTo(""))
        assertThat(articleModel.description, equalTo(""))
        assertThat(articleModel.title, equalTo(ARTICLE_REMOTE_MODEL_EMPTY_FIELDS.title))
        assertThat(articleModel.publishedAt, equalTo(ARTICLE_REMOTE_MODEL_EMPTY_FIELDS.publishedAt))
        assertThat(articleModel.urlToImage, equalTo(""))
    }

    @Test
    fun `converts local article model into a domain`() {
        val articleModel = ARTICLE_LOCAL_MODEL.toDomain()

        assertThat(articleModel.url, equalTo(ARTICLE_LOCAL_MODEL.url))
        assertThat(articleModel.author, equalTo(ARTICLE_LOCAL_MODEL.author))
        assertThat(articleModel.description, equalTo(ARTICLE_LOCAL_MODEL.description))
        assertThat(articleModel.title, equalTo(ARTICLE_LOCAL_MODEL.title))
        assertThat(articleModel.publishedAt, equalTo(ARTICLE_LOCAL_MODEL.publishedAt))
        assertThat(articleModel.urlToImage, equalTo(ARTICLE_LOCAL_MODEL.urlToImage))
    }

    @Test
    fun `converts domain article model into a local`() {
        val articleEntity = ARTICLE_MODEL_1.toEntity()

        assertThat(articleEntity.url, equalTo(ARTICLE_MODEL_1.url))
        assertThat(articleEntity.author, equalTo(ARTICLE_MODEL_1.author))
        assertThat(articleEntity.description, equalTo(ARTICLE_MODEL_1.description))
        assertThat(articleEntity.title, equalTo(ARTICLE_MODEL_1.title))
        assertThat(articleEntity.publishedAt, equalTo(ARTICLE_MODEL_1.publishedAt))
        assertThat(articleEntity.urlToImage, equalTo(ARTICLE_MODEL_1.urlToImage))
    }
}