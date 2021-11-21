package io.github.newsapp

import io.github.newsapp.network.NewsApi
import io.github.newsapp.network.ResponseMapper
import io.github.newsapp.repository.NewsRemoteDataSource
import io.github.newsapp.utils.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class NetworkTest {
    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)

    private val sut = NewsRemoteDataSource(api, ResponseMapper())

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `given 200 response the remote data source should return a DataState#Success with items`() {
        val body = readResponseFile("api-response-success.json")
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        )

        runBlocking {
            val test = sut.getNews().take(2).last()
            assert(test is DataState.Success)
            assert((test as DataState.Success).data.isNotEmpty())
            delay(5000)
        }
    }

    /**
     * Explicit use of !! because it must never happens to be null, else must fix the test
     */
    private fun readResponseFile(filename: String): String {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(filename)

        val source = inputStream!!.let { inputStream.source().buffer() }
        return source.readString(StandardCharsets.UTF_8)
    }
}