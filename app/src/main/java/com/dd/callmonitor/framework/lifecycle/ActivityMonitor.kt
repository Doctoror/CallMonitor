package com.dd.callmonitor.framework.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.annotation.AnyThread

class ActivityMonitor : Application.ActivityLifecycleCallbacks {

    @Volatile
    private var startedActivities = 0

    @AnyThread
    fun getNumberOfStartedActivities() = startedActivities

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityStarted(p0: Activity) {
        startedActivities++
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
        startedActivities--
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }
}
