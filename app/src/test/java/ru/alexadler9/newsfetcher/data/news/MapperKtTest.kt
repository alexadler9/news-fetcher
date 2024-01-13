package ru.alexadler9.newsfetcher.data.news

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.alexadler9.newsfetcher.utility.ARTICLE_LOCAL_MODEL
import ru.alexadler9.newsfetcher.utility.ARTICLE_MODEL_1
import ru.alexadler9.newsfetcher.utility.ARTICLE_REMOTE_MODEL_1
import ru.alexadler9.newsfetcher.utility.ARTICLE_REMOTE_MODEL_EMPTY_FIELDS

class MapperKtTest {

    @Test
    fun `converts remote article model into a domain`() {
        val articleModel = ARTICLE_REMOTE_MODEL_1.toDomain()

        assertEquals(articleModel.url, ARTICLE_REMOTE_MODEL_1.url)
        assertEquals(articleModel.author, ARTICLE_REMOTE_MODEL_1.author)
        assertEquals(articleModel.description, ARTICLE_REMOTE_MODEL_1.description)
        assertEquals(articleModel.title, ARTICLE_REMOTE_MODEL_1.title)
        assertEquals(articleModel.publishedAt, "01.01.2024 12:00")
        assertEquals(articleModel.urlToImage, ARTICLE_REMOTE_MODEL_1.urlToImage)
    }

    @Test
    fun `converts remote article model with empty fields into a domain`() {
        val articleModel = ARTICLE_REMOTE_MODEL_EMPTY_FIELDS.toDomain()

        assertEquals(articleModel.url, ARTICLE_REMOTE_MODEL_EMPTY_FIELDS.url)
        assertEquals(articleModel.author, "")
        assertEquals(articleModel.description, "")
        assertEquals(articleModel.title, ARTICLE_REMOTE_MODEL_EMPTY_FIELDS.title)
        assertEquals(articleModel.publishedAt, "01.01.2024 12:00")
        assertEquals(articleModel.urlToImage, "")
    }

    @Test
    fun `converts local article model into a domain`() {
        val articleModel = ARTICLE_LOCAL_MODEL.toDomain()

        assertEquals(articleModel.url, ARTICLE_LOCAL_MODEL.url)
        assertEquals(articleModel.author, ARTICLE_LOCAL_MODEL.author)
        assertEquals(articleModel.description, ARTICLE_LOCAL_MODEL.description)
        assertEquals(articleModel.title, ARTICLE_LOCAL_MODEL.title)
        assertEquals(articleModel.publishedAt, ARTICLE_LOCAL_MODEL.publishedAt)
        assertEquals(articleModel.urlToImage, ARTICLE_LOCAL_MODEL.urlToImage)
    }

    @Test
    fun `converts domain article model into a local`() {
        val articleEntity = ARTICLE_MODEL_1.toEntity()

        assertEquals(articleEntity.url, ARTICLE_MODEL_1.url)
        assertEquals(articleEntity.author, ARTICLE_MODEL_1.author)
        assertEquals(articleEntity.description, ARTICLE_MODEL_1.description)
        assertEquals(articleEntity.title, ARTICLE_MODEL_1.title)
        assertEquals(articleEntity.publishedAt, ARTICLE_MODEL_1.publishedAt)
        assertEquals(articleEntity.urlToImage, ARTICLE_MODEL_1.urlToImage)
    }
}