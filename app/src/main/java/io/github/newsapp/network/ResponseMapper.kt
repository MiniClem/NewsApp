package io.github.newsapp.network

import io.github.newsapp.model.News
import io.github.newsapp.utils.EntityMapper
import javax.inject.Inject

/**
 * Mapping from response object article to model object
 */
class ResponseMapper
@Inject
constructor() : EntityMapper<Article, News> {
    override fun mapFromEntity(entity: Article): News {
        return News(
            title = entity.title,
            urlToImage = entity.urlToImage,
            description = entity.description,
            urlToArticle = entity.url
        )
    }

    override fun mapFromEntityList(entityList: List<Article>): List<News> {
        return entityList.map { mapFromEntity(it) }
    }
}