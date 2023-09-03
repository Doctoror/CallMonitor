package com.dd.callmonitor.framework.components

import android.app.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class CoroutineScopeService : Service() {

    private val context: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate

    protected val scope = CoroutineScope(context)

    override fun onDestroy() {
        super.onDestroy()
        context.cancel()
    }
}
