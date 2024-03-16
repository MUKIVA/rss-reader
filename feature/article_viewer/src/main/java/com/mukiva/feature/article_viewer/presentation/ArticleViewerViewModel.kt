package com.mukiva.feature.article_viewer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukiva.core.utils.domain.IResult
import com.mukiva.feature.article_viewer.domain.IArticle
import com.mukiva.feature.article_viewer.domain.usecase.LoadArticleUseCase
import com.mukiva.feature.article_viewer.navigation.IArticleViewerRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewerViewModel @Inject constructor(
    initialState: ArticleViewerState,
    private val loadArticleUseCase: LoadArticleUseCase,
    private val router: IArticleViewerRouter
) : ViewModel() {

    val stateFlow by lazy { mStateFlow.asStateFlow() }
    val eventFlow by lazy { mEventFlow.asSharedFlow() }

    private val mStateFlow = MutableStateFlow(initialState)
    private val mEventFlow = MutableSharedFlow<IArticleViewerEvent>()

    fun load(id: Long) {
        viewModelScope.launch {
            when(val result = loadArticleUseCase(id)) {
                is IResult.Error -> onLoadError()
                is IResult.Success -> onLoadSuccess(result.data)
            }
        }
    }

    fun share() {
        viewModelScope.launch {
            val url = stateFlow.value.newsUrl
            mEventFlow.emit(IArticleViewerEvent.Share(url))
        }
    }

    fun openOriginal() {
        viewModelScope.launch {
            val url = stateFlow.value.newsUrl
            mEventFlow.emit(IArticleViewerEvent.OpenOriginal(url))
        }
    }

    fun onBack() {
        viewModelScope.launch {
            router.goBack()
        }
    }

    private fun onLoadSuccess(data: IArticle) {
        mStateFlow.update {
            it.copy(
                type = ArticleViewerState.Type.CONTENT,
                news = data
            )
        }
    }

    private fun onLoadError() {
        mStateFlow.update {
            it.copy(
                type = ArticleViewerState.Type.ERROR
            )
        }
    }
}