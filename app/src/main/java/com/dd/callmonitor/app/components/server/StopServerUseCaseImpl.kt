package com.dd.callmonitor.app.components.server

import android.content.Context
import android.content.Intent
import com.dd.callmonitor.domain.server.StopServerUseCase

class StopServerUseCaseImpl(private val context: Context) : StopServerUseCase {

    override fun invoke() {
        context.startService(
            Intent(context, ServerService::class.java)
                .apply { action = SERVER_SERVICE_ACTION_STOP }
        )
    }
}
