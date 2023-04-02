package com.mukiva.rssreader.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class SingleStateViewModel<TState: Any, TEvent: Any>(
    initialState: TState
) : ViewModel() {

    val state: LiveData<TState> by lazy { _state }
    val event: SharedFlow<TEvent> by lazy { _eventFlow }

    private val _state = MutableLiveData(initialState)
    private val _eventFlow = MutableSharedFlow<TEvent>()

    protected fun modifyState(state: TState) = modifyState { state }

    protected fun modifyState(modifier: TState.() -> TState) {
        _state.value = _state.value?.let(modifier)
    }

    protected fun getState(): TState = _state.value!!

    protected suspend fun event(event: TEvent) {
        _eventFlow.emit(event)
    }
}