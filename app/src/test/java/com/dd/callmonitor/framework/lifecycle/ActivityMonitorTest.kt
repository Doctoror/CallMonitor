package com.dd.callmonitor.framework.lifecycle

import android.app.Activity
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class ActivityMonitorTest {

    private val underTest = ActivityMonitor()

    @Test
    fun numberOfStartedActivitiesZeroByDefault() {
        assertEquals(0, underTest.getNumberOfStartedActivities())
    }

    @Test
    fun numberOfStartedActivitiesIsOneWhenOneStarted() {
        underTest.onActivityStarted(mockk())

        assertEquals(1, underTest.getNumberOfStartedActivities())
    }

    @Test
    fun numberOfStartedActivitiesIsOneWhenTwoStartedAndOneStopped() {
        val activity1: Activity = mockk()
        val activity2: Activity = mockk()
        underTest.onActivityStarted(activity1)
        underTest.onActivityStarted(activity2)
        underTest.onActivityStopped(activity1)

        assertEquals(1, underTest.getNumberOfStartedActivities())
    }

    @Test
    fun numberOfStartedActivitiesIsTwoWhenTwoStarted() {
        val activity1: Activity = mockk()
        val activity2: Activity = mockk()
        underTest.onActivityStarted(activity1)
        underTest.onActivityStarted(activity2)

        assertEquals(2, underTest.getNumberOfStartedActivities())
    }

    @Test
    fun numberOfStartedActivitiesIsZeroWhenTwoStartedTwoStopped() {
        val activity1: Activity = mockk()
        val activity2: Activity = mockk()
        underTest.onActivityStarted(activity1)
        underTest.onActivityStarted(activity2)
        underTest.onActivityStopped(activity2)
        underTest.onActivityStopped(activity1)

        assertEquals(0, underTest.getNumberOfStartedActivities())
    }
}
