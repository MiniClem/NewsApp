package io.github.newsapp.network

import io.github.newsapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("X-Api-Key", BuildConfig.NEWS_API_KEY)
                .build()
        )
    }
}