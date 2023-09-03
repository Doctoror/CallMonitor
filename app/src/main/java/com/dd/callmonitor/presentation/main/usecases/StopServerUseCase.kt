package com.dd.callmonitor.presentation.main.usecases

import android.content.Context
import android.content.Intent
import com.dd.callmonitor.data.server.SERVER_SERVICE_ACTION_STOP
import com.dd.callmonitor.data.server.ServerService

class StopServerUseCase(private val context: Context) {

    operator fun invoke() {
        context.startService(
            Intent(context, ServerService::class.java)
                .apply { action = SERVER_SERVICE_ACTION_STOP }
        )
    }
}
