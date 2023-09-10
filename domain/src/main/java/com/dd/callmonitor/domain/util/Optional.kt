package com.dd.callmonitor.domain.util

/**
 * Minimalistic version of [java.util.Optional] for compatibility with
 * [android.os.Build.VERSION_CODES.M]
 */
data class Optional<T>(private val value: T?) {

    fun get(): T = value ?: throw NoSuchElementException()

    fun isPresent() = value != null

    fun <U> flatMap(mapper: (T) -> Optional<U>): Optional<U> = if (!isPresent()) {
        empty()
    } else {
        mapper(value!!)
    }

    fun orElse(other: T): T = value ?: other

    companion object {

        fun <T> empty() = Optional<T>(null)

        fun <T> of(value: T) = Optional(value)

        fun <T> ofNullable(value: T?) = Optional(value)
    }
}
