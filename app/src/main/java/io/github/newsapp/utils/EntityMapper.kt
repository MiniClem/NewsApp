package io.github.newsapp.utils

/**
 * Mapping from object to another
 */
interface EntityMapper<in I, out O> {
    fun mapFromEntity(entity: I): O
    fun mapFromEntityList(entityList: List<I>): List<O>
}