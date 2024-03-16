package com.mukiva.feature.article_viewer.presentation

import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.article_viewer.domain.IArticle
import java.util.Date

data class ArticleViewerState(
    val type: Type,
    val news: IArticle
) : IArticle by news {
    enum class Type {
        LOADING,
        CONTENT,
        ERROR
    }

    companion object {
        fun default() = ArticleViewerState(
            type = Type.LOADING,
            news = object : IArticle {
                override val title: String
                    get() = ""
                override val pubDate: Date
                    get() = Date()
                override val imgUrl: String
                    get() = ""
                override val content: String
                    get() = ""
                override val newsUrl: String
                    get() = ""

            }
        )

        @DebugOnly
        fun mock() = ArticleViewerState(
            type = Type.CONTENT,
            news = object : IArticle {
                override val title: String
                    get() = "Mock title"
                override val pubDate: Date
                    get() = Date()
                override val imgUrl: String
                    get() = "https://s3-alpha-sig.figma.com/img/0bee/e8d3/fd6407b47cff3f7cd84d879b8392ed1c?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=OyVTp9EZcsH7inG-QJMOJ-58Gf2tN4nyArmZZ7r8dml3SUIpqCLmItkqfL8JGvYTm5otey0KdVeaT19HkkkmhY0DlwHXrOdiQNdN56IlGnVxPbp9n7Neo62-PuQglj6C64DZSsO5A90dKbggKnfh2X6gwvpm2y5LnqDW-sxFyMIuDW3K5q3xFqsAQ5j9~psrc5MOpQGHDyJrxzP3NZVXgFloABDERu8CpP3hme2~eM2Vcvng2w7Vd7SfgnIpN19AB5gQ3R7GlE~vq7gtXSu-1QuLGZoHQORQDY33I-Me-8AQE8oYaq7JhPz6AuuGM4WJOHzCQ4s6C4eYH5pmduiF9g__"
                override val content: String
                    get() = "Mock content"
                override val newsUrl: String
                    get() = "https://google.com"

            }
        )
    }
}