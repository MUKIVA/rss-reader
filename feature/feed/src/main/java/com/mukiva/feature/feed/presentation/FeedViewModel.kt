package com.mukiva.feature.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukiva.core.ui.Paginator
import com.mukiva.core.utils.domain.IResult
import com.mukiva.core.utils.domain.IResultError
import com.mukiva.feature.feed.domain.IArticle
import com.mukiva.feature.search.common.domian.IRssMeta
import com.mukiva.feature.feed.domain.usecase.DeleteRssUseCase
import com.mukiva.feature.feed.domain.usecase.ForceUpdateFeedUseCase
import com.mukiva.feature.feed.domain.usecase.GetRssItemsPageUseCase
import com.mukiva.feature.feed.domain.usecase.GetRssMetaDataUseCase
import com.mukiva.feature.feed.navigation.IFeedRouter
import com.mukiva.feature.search.common.domian.IRss
import com.mukiva.feature.search.common.presentation.GlobalSearchEventNotifier
import com.mukiva.feature.search.common.presentation.ISearchEvent
import com.mukiva.feature.search.common.presentation.ISearchStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    initialState: FeedScreenState,
    searchStateHolder: ISearchStateHolder,
    private val feedRouter: IFeedRouter,
    private val deleteRssUseCase: DeleteRssUseCase,
    private val getRssMetaDataUseCase: GetRssMetaDataUseCase,
    private val requestRssPage: GetRssItemsPageUseCase,
    private val forceUpdateRss: ForceUpdateFeedUseCase,
) : ViewModel(), ISearchStateHolder by searchStateHolder  {

    val stateFlow by lazy { mStateFlow.asStateFlow() }
    val eventFlow by lazy { mEventFlow.asSharedFlow() }

    private val mStateFlow = MutableStateFlow(initialState)
    private val mEventFlow = MutableSharedFlow<IFeedEvent>()

    private var mPaginatorList: List<Paginator<Int, IArticle>> = emptyList()

    init {

        @Suppress("OPT_IN_USAGE")
        searchStateHolder.searchStateFlow
            .map { it.value }
            .debounce(ISearchStateHolder.SEARCH_DEBOUNCE)
            .onEach { search(it, viewModelScope) }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            loadMetaData()

            GlobalSearchEventNotifier.eventFlow
                .collect { (notifier, event) ->
                    if (notifier == searchStateHolder)
                        return@collect
                    when(event) {
                        ISearchEvent.AddEvent -> loadMetaData()
                    }
                }
        }



    }

    fun scrollToPage(index: Int) {
        viewModelScope.launch {
            mEventFlow.emit(IFeedEvent.ScrollToPage(index))
        }
    }

    fun goSearch() {
        viewModelScope.launch {
            feedRouter.goSearch()
        }
    }

    fun goNewsViewer(newsId: Long) {
        viewModelScope.launch {
            feedRouter.goNewsViewer(newsId)
        }
    }

    fun addRss() {
        addRss(viewModelScope
            , onSuccess = { loadMetaData() }
            , onError = { mEventFlow.emit(IFeedEvent.UnknownError) }
        )
    }

    fun deleteRss(rssIndex: Int) {
        viewModelScope.launch {
            val meta = mStateFlow.value.pages
                .elementAt(rssIndex)
                .meta
            when(deleteRssUseCase(meta)) {
                is IResult.Error -> mEventFlow.emit(IFeedEvent.UnknownError)
                is IResult.Success -> loadMetaData()
            }
        }
    }

    fun loadNextPage(feedIndex: Int) {
        viewModelScope.launch {
            mPaginatorList.elementAt(feedIndex).loadNextItems()
        }
    }

    suspend fun forceUpdateFeed(feedIndex: Int) {
        val feed = stateFlow.value.pages.elementAt(feedIndex)

        when(val result = forceUpdateRss(feed.originalUrl)) {
            is IResult.Error -> onErrorUpdateFeed(result.resultError)
            is IResult.Success -> onSuccessUpdateFeed(feedIndex, result.data)
        }
        mPaginatorList.elementAt(feedIndex).reset()
        mPaginatorList.elementAt(feedIndex).loadNextItems()
    }

    private fun onSuccessUpdateFeed(feedIndex: Int, data: IRss) {
        mStateFlow.update {
            it.copy(
                pages = it.pages.transformAt(feedIndex) {
                    copy(
                        meta = data,
                        articleStateCollection = emptyList()
                    )
                }
            )
        }
    }

    private suspend fun onErrorUpdateFeed(err: IResultError) {
        when (err) {
            IResultError.ConnectionError -> mEventFlow.emit(IFeedEvent.InternetConnectionError)
            else -> mEventFlow.emit(IFeedEvent.UnknownError)
        }
    }

    private suspend fun loadMetaData() {
        when(val result = getRssMetaDataUseCase()) {
            is IResult.Error -> onErrorLoadMetaData()
            is IResult.Success -> onSuccessLoadMetaData(result.data)
        }
    }

    private fun onSuccessLoadMetaData(data: Collection<IRssMeta>) {
        mPaginatorList = buildList {
            data.forEachIndexed { index, _ -> add(createPaginator(index)) }
        }

        mStateFlow.update {
            it.copy(
                type = if (data.isEmpty())
                    FeedScreenState.Type.EMPTY
                else
                    FeedScreenState.Type.CONTENT,
                pages = data.mapIndexed { index, meta ->
                    FeedPage(
                        type = FeedPage.Type.LOADING,
                        meta = meta,
                        articleStateCollection = emptyList(),
                        isSelected =  index == 0,
                        bottomProgressType = FeedPagnator.ProgressType.CONTENT
                    )
                }
            )
        }
    }

    private fun createPaginator(feedIndex: Int): Paginator<Int, IArticle> {
        return FeedPagnator(
            initialKey = 0,
            onLoadUpdated = { onPagerLoadUpdated(feedIndex, it) },
            onRequest = { nextKey ->
                val feed = mStateFlow.value.pages.elementAt(feedIndex)
                requestRssPage(feed.originalUrl, nextKey, PAGE_SIZE)
            },
            onError = { mEventFlow.emit(IFeedEvent.UnknownError) },
            onSuccess = { items, _ -> onPagerSuccess(feedIndex, items)},
            getNextKey = { prevKey ->
                prevKey + 1
            }
        )
    }

    private fun onPagerLoadUpdated(feedIndex: Int, progressType: FeedPagnator.ProgressType) {
        mStateFlow.update {
            val itemsCount = it.pages.elementAt(feedIndex).articleStateCollection.size
            it.copy(pages = it.pages.transformAt(feedIndex) {
                copy(
                    type = when {
                        itemsCount == 0 && progressType == FeedPagnator.ProgressType.ERROR -> FeedPage.Type.ERROR
                        itemsCount == 0 && progressType == FeedPagnator.ProgressType.CONTENT -> FeedPage.Type.EMPTY
                        itemsCount == 0 && progressType == FeedPagnator.ProgressType.LOADING -> FeedPage.Type.LOADING
                        else -> FeedPage.Type.CONTENT
                    },
                    bottomProgressType = progressType
                )
            })
        }
    }

    private fun onPagerSuccess(feedIndex: Int, items: Collection<IArticle>) {
        mStateFlow.update {
            it.copy(
                pages = it.pages.transformAt(feedIndex) {
                    copy(
                        type = FeedPage.Type.CONTENT,
                        articleStateCollection = it.pages.elementAt(feedIndex).articleStateCollection + items
                    )
                }
            )
        }
    }

    private fun onErrorLoadMetaData() {
        mStateFlow.update {
            it.copy(
                type = FeedScreenState.Type.ERROR
            )
        }
    }

    private fun <T> Collection<T>.transformAt(indexToTransform: Int, transform: T.() -> T): Collection<T> {
        return mapIndexed { index, item ->
            if (indexToTransform == index)
                item.transform()
            else
                item
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}