package com.dd.callmonitor.domain.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OptionalTest {

    @Test(expected = NoSuchElementException::class)
    fun getThrowsNoSuchElementExceptionWhenEmpty() {
        Optional.empty<Any>().get()
    }

    @Test(expected = NoSuchElementException::class)
    fun getThrowsNoSuchElementExceptionWhenCreatedWithNullValue() {
        Optional.ofNullable<Any>(null).get()
    }

    @Test
    fun getReturnsValue() {
        val value = 1333
        assertEquals(value, Optional.of(value).get())
    }

    @Test
    fun isPresentWhenHasValue() {
        assertTrue(Optional.of(Unit).isPresent())
    }

    @Test
    fun isNotPresentWhenEmpty() {
        assertFalse(Optional.empty<Any>().isPresent())
    }

    @Test
    fun isNotPresentWhenCreatedFromNull() {
        assertFalse(Optional.ofNullable<Any>(null).isPresent())
    }

    @Test
    fun mapReturnsEmptyWhenEmpty() {
        assertEquals(
            Optional.empty<Int>(),
            Optional.empty<Int>().map { 1 }
        )
    }

    @Test
    fun mapReturnsMappedValueWhenNotEmpty() {
        assertEquals(
            Optional.of(2),
            Optional.of(1).map { 2 }
        )
    }

    @Test
    fun flatMapReturnsEmptyWhenEmpty() {
        assertEquals(
            Optional.empty<Int>(),
            Optional.empty<Int>().flatMap { Optional.of(2) }
        )
    }

    @Test
    fun flatMapReturnsMappedValueWhenNotEmpty() {
        assertEquals(
            Optional.of(2),
            Optional.of(1).flatMap { Optional.of(2) }
        )
    }

    @Test
    fun orReturnsInitialValueIfPresent() {
        assertEquals(
            Optional.of(1),
            Optional.of(1).or { Optional.of(2) }
        )
    }

    @Test
    fun orReturnsNextValueIfEmpty() {
        assertEquals(
            Optional.of(2),
            Optional.empty<Int>().or { Optional.of(2) }
        )
    }

    @Test
    fun orElseReturnsInitialValueIfPresent() {
        assertEquals(
            1,
            Optional.of(1).orElse(2)
        )
    }

    @Test
    fun orElseReturnsNextValueIfEmpty() {
        assertEquals(
            2,
            Optional.empty<Int>().orElse(2)
        )
    }
}
