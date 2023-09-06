package com.dd.callmonitor.app.components.server

import android.content.Context
import android.content.Intent
import com.dd.callmonitor.domain.server.StartServerUseCase

class StartServerUseCaseImpl(private val context: Context) : StartServerUseCase {

    override fun invoke() {
        context.startService(
            Intent(context, ServerService::class.java)
                .apply { action = SERVER_SERVICE_ACTION_START }
        )
    }
}
