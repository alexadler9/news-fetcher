package ru.alexadler9.newsfetcher.data.news.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import ru.alexadler9.newsfetcher.data.news.remote.type.ArticlesCategoryRemote
import ru.alexadler9.newsfetcher.data.news.remote.type.ArticlesCountryRemote
import ru.alexadler9.newsfetcher.data.news.toDomain
import ru.alexadler9.newsfetcher.domain.model.ArticleModel

class NewsPagingRemoteSource @AssistedInject constructor(
    private val newsApi: NewsApi,
    @Assisted("country") private val country: ArticlesCountryRemote,
    @Assisted("category") private val category: ArticlesCategoryRemote,
    @Assisted("query") private val query: String
) : PagingSource<Int, ArticleModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleModel> {
        try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize.coerceAtMost(NewsApi.PAGE_SIZE_MAX)
            val response = newsApi.getTopHeadlinesArticles(
                country = country,
                category = category,
                query = query,
                pageSize = pageSize,
                page = pageNumber
            )
            val articles = response.articleList.filter {
                it.title != "[Removed]" && it.description != ""
            }.map {
                it.toDomain()
            }
            val nextPageNumber = if (response.articleList.isEmpty()) null else pageNumber + 1
            val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
            return LoadResult.Page(articles, prevPageNumber, nextPageNumber)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("country") country: ArticlesCountryRemote,
            @Assisted("category") category: ArticlesCategoryRemote,
            @Assisted("query") query: String
        ): NewsPagingRemoteSource
    }
}