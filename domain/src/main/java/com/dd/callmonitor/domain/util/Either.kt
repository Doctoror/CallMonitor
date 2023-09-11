package com.dd.callmonitor.domain.util

/**
 * Can be used like [Result], but where [Result.failure] doesn't have to contain [Throwable].
 *
 * Can be considered as a simplified version of
 * https://github.com/arrow-kt/arrow-core/blob/master/arrow-core-data/src/main/kotlin/arrow/core/Either.kt
 */
class Either<L, R> private constructor(
    private val left: L?,
    private val right: R?
) {

    fun <T> fold(
        onLeft: (value: L) -> T,
        onRight: (value: R) -> T
    ): T = if (left != null) {
        onLeft(left)
    } else {
        onRight(right!!)
    }

    companion object {

        fun <L, R> left(left: L) = Either<L, R>(left, null)

        fun <L, R> right(right: R) = Either<L, R>(null, right)
    }
}
