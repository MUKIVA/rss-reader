package com.mukiva.feature.feed.presentation

import com.mukiva.feature.feed.domain.IArticle
import com.mukiva.feature.search.common.domian.IRssMeta

data class FeedPage(
    val type: Type,
    val bottomProgressType: FeedPagnator.ProgressType,
    val meta: IRssMeta,
    val articleStateCollection: Collection<IArticle>,
    val isSelected: Boolean
) : IRssMeta by meta {
    enum class Type {
        EMPTY,
        LOADING,
        CONTENT,
        ERROR
    }
}