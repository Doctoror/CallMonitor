package com.dd.callmonitor.domain.util

/**
 * Can be used like [Result], but where [Result.failure] doesn't have to contain [Throwable].
 *
 * Can be considered as a simplified version of
 * https://github.com/arrow-kt/arrow-core/blob/master/arrow-core-data/src/main/kotlin/arrow/core/Either.kt
 *
 * Cannot be a `data` class because we cannot support `copy` functionality which will break the
 * contract by allowing to set both values to null or non-null.
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

    override fun equals(other: Any?) = this === other || (
            other is Either<*, *> &&
                    other.left == left &&
                    other.right == right
            )

    /**
     * Auto-generated
     */
    override fun hashCode(): Int {
        var result = left?.hashCode() ?: 0
        result = 31 * result + (right?.hashCode() ?: 0)
        return result
    }

    override fun toString() = "Either(left = $left, right = $right)"

    companion object {

        fun <L, R> left(left: L) = Either<L, R>(left, null)

        fun <L, R> right(right: R) = Either<L, R>(null, right)
    }
}
