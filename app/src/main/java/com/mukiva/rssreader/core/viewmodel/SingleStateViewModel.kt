package com.mukiva.rssreader.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class SingleStateViewModel<TState: Any>(
    initialState: TState
) : ViewModel() {

    val state: LiveData<TState> by lazy { _state }

    private val _state = MutableLiveData(initialState)

    protected fun modifyState(state: TState) = modifyState { state }

    protected fun modifyState(modifier: TState.() -> TState) {
        _state.value = _state.value?.let(modifier)
    }

    protected fun getState(): TState = _state.value!!
}