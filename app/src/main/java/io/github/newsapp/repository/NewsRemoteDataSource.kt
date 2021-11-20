package io.github.newsapp.repository

import io.github.newsapp.model.News
import io.github.newsapp.network.DataState
import io.github.newsapp.network.NewsApi
import io.github.newsapp.network.ResponseMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class NewsRemoteDataSource
@Inject
constructor(
    private val newsApi: NewsApi,
    private val newsMapper: ResponseMapper
) {
    suspend fun getNews(): Flow<DataState<List<News>>> = flow {
        emit(DataState.Loading)
        newsApi.runCatching { getEverything().execute() }
            .onSuccess { news ->
                if (news.isSuccessful)
                    emit(
                        DataState.Success(
                            if (news.body() != null) newsMapper.mapFromEntityList(news.body()!!.articles)
                            else emptyList()
                        )
                    )
                else
                    emit(DataState.Error(HttpException(news)))
            }
            .onFailure { emit(DataState.Error(it)) }
    }
}
