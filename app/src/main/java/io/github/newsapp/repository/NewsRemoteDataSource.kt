package io.github.newsapp.repository

import io.github.newsapp.model.News
import io.github.newsapp.network.NewsApi
import io.github.newsapp.network.ResponseMapper
import io.github.newsapp.utils.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Remote data source for articles using the News Api for a viewmodel
 */
class NewsRemoteDataSource
@Inject
constructor(
    private val newsApi: NewsApi,
    private val newsMapper: ResponseMapper
) {
    suspend fun getNews(): Flow<DataState<List<News>>> = flow {
        emit(DataState.Loading)
        newsApi.runCatching { withContext(Dispatchers.IO) { getEverything("kotlin").execute() } }
            .onSuccess { news ->
                if (news.isSuccessful)
                    emit(
                        DataState.Success(
                            if (news.body() != null) newsMapper.mapFromEntityList(news.body()!!.articles)
                            else emptyList()
                        )
                    )
                else {
                    System.err.println(news.code())
                    System.err.println(news.message())
                    emit(DataState.Error(HttpException(news)))
                }
            }
            .onFailure {
                emit(DataState.Error(it))
            }
    }
}
