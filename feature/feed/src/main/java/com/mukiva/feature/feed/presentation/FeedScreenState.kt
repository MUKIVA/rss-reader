package com.mukiva.feature.feed.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.feed.domain.IArticle
import com.mukiva.feature.search.common.domian.IRssMeta
import java.util.Date

data class FeedScreenState(
    val type: Type,
    val pages: Collection<FeedPage>
) {

    enum class Type {
        LOADING,
        EMPTY,
        ERROR,
        CONTENT
    }

    val selectedPageIndex
        get() = pages.indexOfFirst { it.isSelected }

    @OptIn(ExperimentalFoundationApi::class)
    val asPagerState
        get() = object : PagerState(selectedPageIndex, 0f) {
            override val pageCount: Int
                get() = pages.size

        }

    companion object {
        fun default() = FeedScreenState(
            type = Type.LOADING,
            pages = emptyList()
        )

        @DebugOnly
        fun mock() = FeedScreenState(
            type = Type.CONTENT,
            pages = buildList {
                add(FeedPage(
                    type = FeedPage.Type.LOADING,
                    meta = object : IRssMeta {
                        override val name: String = "Sample one"
                        override val url: String = ""
                        override val originalUrl: String = ""
                        override val description: String = "Description"
                        override val imageUrl: String = "Image URL"
                    },
                    articleStateCollection = buildList {
                        add(
                            object : IArticle {
                                override val id: Long
                                    get() = 0
                                override val title: String
                                    get() = "Label 1"
                                override val body: String
                                    get() = "Body 1"
                                override val date: Date
                                    get() = Date()
                                override val imgUrl: String
                                    get() = ""

                            }
                        )
                        add(
                            object : IArticle {
                                override val id: Long
                                    get() = 0
                                override val title: String
                                    get() = "Label 1"
                                override val body: String
                                    get() = "Body 1"
                                override val date: Date
                                    get() = Date()
                                override val imgUrl: String
                                    get() = ""

                            }
                        )
                    },
                    isSelected = true,
                    bottomProgressType = FeedPagnator.ProgressType.CONTENT
                ))
                add(FeedPage(
                    type = FeedPage.Type.LOADING,
                    meta = object : IRssMeta {
                        override val name: String = "Sample two"
                        override val url: String = ""
                        override val originalUrl: String = ""
                        override val description: String = "Description"
                        override val imageUrl: String = "Image URL"
                    },
                    articleStateCollection = buildList {
                        add(
                            object : IArticle {
                                override val id: Long
                                    get() = 0
                                override val title: String
                                    get() = "Label 1"
                                override val body: String
                                    get() = "Body 1"
                                override val date: Date
                                    get() = Date()
                                override val imgUrl: String
                                    get() = ""

                            }
                        )
                        add(
                            object : IArticle {
                                override val id: Long
                                    get() = 0
                                override val title: String
                                    get() = "Label 1"
                                override val body: String
                                    get() = "Body 1"
                                override val date: Date
                                    get() = Date()
                                override val imgUrl: String
                                    get() = ""

                            }
                        )
                    },
                    isSelected = false,
                    bottomProgressType = FeedPagnator.ProgressType.CONTENT
                ))
            }
        )
    }
}