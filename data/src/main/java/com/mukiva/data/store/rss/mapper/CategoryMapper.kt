package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.Category
import com.mukiva.data.store.rss.entity.CategoryEntity
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun mapAsEntity(category: Category) = CategoryEntity(
        id = category.id,
        text = category.text,
        domain = category.domain,
    )


    fun mapAsDomain(entity: CategoryEntity) = Category(
        id = entity.id,
        text = entity.text,
        domain = entity.domain,
    )

}