package com.dd.callmonitor.domain.util

/**
 * Minimalistic version of [java.util.Optional] for compatibility with
 * [android.os.Build.VERSION_CODES.M]
 */
data class Optional<T>(private val value: T?) {

    fun get(): T = value ?: throw NoSuchElementException()

    fun isPresent() = value != null

    companion object {

        fun <T> empty() = Optional<T>(null)

        fun <T> of(value: T) = Optional(value)
    }
}
