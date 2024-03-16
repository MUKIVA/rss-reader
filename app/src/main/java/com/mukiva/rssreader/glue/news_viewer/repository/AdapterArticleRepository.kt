package com.mukiva.rssreader.glue.news_viewer.repository

import com.mukiva.data.repository.IRssRepository
import com.mukiva.feature.article_viewer.domain.IArticle
import com.mukiva.feature.article_viewer.domain.repository.IArticleRepository
import java.util.Date
import javax.inject.Inject

class AdapterArticleRepository @Inject constructor(
    private val globalRepo: IRssRepository
) : IArticleRepository {
    override suspend fun loadNewsById(id: Long): IArticle {
        val item = globalRepo.getNewsById(id)
        return object : IArticle {
            override val title: String
                get() = item.title ?: ""
            override val pubDate: Date
                get() = item.pubDate ?: Date()
            override val imgUrl: String
                get() = item.enclosure?.url ?: ""
            override val content: String
                get() = item.description
            override val newsUrl: String
                get() = item.link ?: ""

        }
    }
}