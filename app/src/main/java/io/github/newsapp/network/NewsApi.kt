package io.github.newsapp.network

import io.github.newsapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi {
    @GET("https://newsapi.org/v2/everything")
    suspend fun getEverything(
        @Query("language") language: String = "fr",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Header("X-Api-Key") apikey: String = BuildConfig.NEWS_API_KEY
    ): ResponseObject
}