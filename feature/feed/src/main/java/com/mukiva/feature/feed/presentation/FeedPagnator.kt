package com.mukiva.feature.feed.presentation

import com.mukiva.core.ui.Paginator

class FeedPagnator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (ProgressType) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<Collection<Item>>,
    private inline val getNextKey: suspend (Key) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: Collection<Item>, newKey: Key) -> Unit
) : Paginator<Key, Item> {

    private var mCurrentKey = initialKey
    private var mIsMakingRequest = false

    enum class ProgressType {
        CONTENT,
        ERROR,
        LOADING
    }

    override suspend fun loadNextItems() {
        if (mIsMakingRequest) {
            return
        }
        mIsMakingRequest = true
        onLoadUpdated(ProgressType.LOADING)
        val result = onRequest(mCurrentKey)
        mIsMakingRequest = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(ProgressType.ERROR)
            return
        }
        if (items.isNotEmpty())
            mCurrentKey = getNextKey(mCurrentKey)
        onSuccess(items, mCurrentKey)
        onLoadUpdated(ProgressType.CONTENT)
    }

    override fun reset() {
        mCurrentKey = initialKey
    }


}