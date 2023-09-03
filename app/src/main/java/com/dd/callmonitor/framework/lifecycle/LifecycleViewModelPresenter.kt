package com.dd.callmonitor.framework.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

/**
 * Note for code reviewers:
 *
 * Having a "base class" is controversial. It is risking turning into a "god object" given long
 * enough project lifespan, so it is indeed crucial to be careful about what functionality will be
 * added there.
 *
 * However, I see great benefit of easily reusing the [LifecycleEventObserver] to forward
 * [onCreate]. To address "favor inheritance over composition" argument, this cannot really be
 * achieved by composition.
 */
abstract class LifecycleViewModelPresenter : ViewModel(), LifecycleEventObserver {

    abstract fun onCreate()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            onCreate()
        }
    }
}
