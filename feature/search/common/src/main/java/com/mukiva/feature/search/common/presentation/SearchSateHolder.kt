package com.mukiva.feature.search.common.presentation

import com.mukiva.core.utils.domain.IResult
import com.mukiva.core.utils.domain.IResultError
import com.mukiva.feature.search.common.domian.IRss
import com.mukiva.feature.search.common.domian.IRssMeta
import com.mukiva.feature.search.common.domian.usecase.AddRssUseCase
import com.mukiva.feature.search.common.domian.usecase.RequestRssItemsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias SearchErrorType = SearchState.IResultState.ErrorState.ErrorType
class SearchSateHolder @Inject constructor(
    initialState: SearchState,
    private val addRssUseCase: AddRssUseCase,
    private val requestRssItemsUseCase: RequestRssItemsUseCase
) : ISearchStateHolder {

    override val searchStateFlow by lazy { mSearchStateFlow.asStateFlow() }

    private var mSearchCacheRss: IRssMeta? = null

    private val mSearchStateFlow = MutableStateFlow(initialState)
    private var mOldSearchValue: String = initialState.value

    override fun updateValue(value: String) {
        mSearchStateFlow.update {
            it.copy(value = value)
        }
    }

    override fun search(url: String, scope: CoroutineScope) {
        if (url == mOldSearchValue) return
        mOldSearchValue = url
        if (url.isBlank()) {
            updateStateType(SearchState.IResultState.Empty)
            return
        }
        updateStateType(SearchState.IResultState.Loading)

        scope.launch {
            when(val result = requestRssItemsUseCase(url, checkMetaContains = true)) {
                is IResult.Error -> when (result.resultError) {
                    IResultError.ConnectionError -> updateStateType(
                        SearchState.IResultState.ErrorState(SearchErrorType.CONNECTION)
                    )
                    IResultError.InvalidUrlError -> updateStateType(
                        SearchState.IResultState.ErrorState(SearchErrorType.INVALID_URL)
                    )

                    IResultError.UnknownError -> updateStateType(
                        SearchState.IResultState.ErrorState(SearchErrorType.UNKNOWN)
                    )

                    IResultError.TimeoutError -> updateStateType(
                        SearchState.IResultState.ErrorState(SearchErrorType.TIME_OUT)
                    )

                    IResultError.UrlAlreadyExists -> updateStateType(
                        SearchState.IResultState.ErrorState(SearchErrorType.URL_ALREADY_EXISTS)
                    )

                }
                is IResult.Success -> setFoundRss(result.data)
            }
        }
    }

    override fun addRss(
        scope: CoroutineScope,
        onSuccess: suspend () -> Unit,
        onError: suspend (IResultError) -> Unit
    ) {
        if (mSearchCacheRss == null) return

        scope.launch {
            when(val result = addRssUseCase.invoke(mSearchCacheRss!!)) {
                is IResult.Error -> onError(result.resultError)
                is IResult.Success -> {
                    updateValue("")
                    updateStateType(SearchState.IResultState.Empty)
                    onSuccess()
                }
            }

            GlobalSearchEventNotifier.notify(this@SearchSateHolder, ISearchEvent.AddEvent)
        }
    }

    private fun setFoundRss(rss: IRss) {
        mSearchCacheRss = rss
        updateStateType(
            SearchState.IResultState.SuccessState(
                title = rss.name,
                description = rss.description,
                imageUrl = rss.imageUrl,
            )
        )
    }

    private fun updateStateType(type: SearchState.IResultState) {
        mSearchStateFlow.update {
            it.copy(resultState = type)
        }
    }
}