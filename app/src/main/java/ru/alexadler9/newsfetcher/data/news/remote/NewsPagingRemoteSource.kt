package ru.alexadler9.newsfetcher.data.news.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.alexadler9.newsfetcher.data.news.toDomain
import ru.alexadler9.newsfetcher.domain.model.ArticleModel
import javax.inject.Inject

class NewsPagingRemoteSource @Inject constructor(
    private val newsApi: NewsApi
) : PagingSource<Int, ArticleModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleModel> {
        try {
            val pageNumber = params.key ?: 1
            val pageSize = params.loadSize.coerceAtMost(NewsApi.PAGE_SIZE_MAX)
            val response = newsApi.getTopHeadlinesArticles(pageSize = pageSize, page = pageNumber)
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
}