package com.mukiva.feature.search.common.presentation

data class SearchState(
    val value: String,
    val resultState: IResultState
) {
    sealed interface IResultState {

        data object Empty : IResultState

        data object Loading : IResultState

        data class ErrorState(
            val errorType: ErrorType
        ) : IResultState {
            enum class ErrorType {
                UNKNOWN,
                INVALID_URL,
                CONNECTION,
                URL_ALREADY_EXISTS,
                TIME_OUT
            }
        }

        data class SuccessState(
            val title: String,
            val description: String,
            val imageUrl: String
        ) : IResultState
    }

    companion object {
        fun default() = SearchState(
            value = "",
            resultState = IResultState.Empty
        )
    }
}