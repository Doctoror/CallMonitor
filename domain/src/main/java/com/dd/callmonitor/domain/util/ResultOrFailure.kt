package com.dd.callmonitor.domain.util

class ResultOrFailure<R, F> private constructor(
    private val result: R?,
    private val failure: F?
) {

    fun <T> fold(
        onSuccess: (value: R) -> T,
        onFailure: (error: F) -> T
    ): T = if (result != null) {
        onSuccess(result)
    } else {
        onFailure(failure!!)
    }

    companion object {

        fun <R, F> success(value: R) = ResultOrFailure<R, F>(value, null)

        fun <R, F> failure(failure: F) = ResultOrFailure<R, F>(null, failure)
    }
}
