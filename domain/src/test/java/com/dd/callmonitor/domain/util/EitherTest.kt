package com.dd.callmonitor.domain.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class EitherTest {

    @Test
    fun equalWhenLeftEqual() {
        assertEquals(
            Either.left<Int, Int>(1),
            Either.left<Int, Int>(1)
        )
    }

    @Test
    fun equalWhenRightEqual() {
        assertEquals(
            Either.right<Int, Int>(2),
            Either.right<Int, Int>(2)
        )
    }

    @Test
    fun notEqualWhenValuesDiffer() {
        assertNotEquals(
            Either.left<Int, Int>(0),
            Either.left<Int, Int>(1)
        )
    }

    @Test
    fun notEqualWhenComparingLeftToRight() {
        assertNotEquals(
            Either.left<Int, Int>(1),
            Either.right<Int, Int>(1)
        )
    }

    @Test
    fun hashCodeMatchesForEqualLefts() {
        assertEquals(
            Either.left<Int, Int>(1).hashCode(),
            Either.left<Int, Int>(1).hashCode()
        )
    }

    @Test
    fun hashCodeMatchesForEqualRights() {
        assertEquals(
            Either.right<Int, Int>(3).hashCode(),
            Either.right<Int, Int>(3).hashCode()
        )
    }

    @Test
    fun hashCodeDiffersForDifferentValues() {
        assertNotEquals(
            Either.left<Int, Int>(0).hashCode(),
            Either.left<Int, Int>(1).hashCode()
        )
    }

    @Test
    fun hashCodeDiffersWhenComparingLeftToRight() {
        assertNotEquals(
            Either.left<Int, Int>(1).hashCode(),
            Either.right<Int, Int>(1).hashCode()
        )
    }
}
