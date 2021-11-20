package io.github.newsapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("everything")
    fun getEverything(
        @Query("q") q: String,
        @Query("language") language: String = "fr",
        @Query("sortBy") sortBy: String = "publishedAt"
    ): Call<ResponseObject>
}