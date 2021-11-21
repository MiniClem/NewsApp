package io.github.newsapp.model

/**
 * The model to be used in the application
 */
data class News(
    val title: String?,
    val urlToImage: String?,
    val description: String?,
    val urlToArticle: String?
)