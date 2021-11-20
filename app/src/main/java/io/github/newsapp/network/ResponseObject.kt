package io.github.newsapp.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseObject(
    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("code")
    @Expose
    val code: String?,

    @SerializedName("message")
    @Expose
    val message: String?,

    @SerializedName("totalResults")
    @Expose
    val totalResults: String,

    @SerializedName("articles")
    @Expose
    val articles: List<Article>
)