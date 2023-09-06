package com.dd.callmonitor.data.server

import com.dd.callmonitor.data.server.routes.log.CallLogResponseMapper
import com.dd.callmonitor.data.server.routes.log.LogRouteRegistrator
import com.dd.callmonitor.data.server.routes.root.ResponseTimeFormatter
import com.dd.callmonitor.data.server.routes.root.RootRouteRegistrator
import com.dd.callmonitor.domain.calls.GetCallLogUseCase
import com.dd.callmonitor.domain.server.Server
import com.dd.callmonitor.domain.server.ServerStateProvider
import java.util.Locale

class ServerFactory {

    fun newInstance(
        getCallLogUseCase: GetCallLogUseCase,
        serverStateProvider: ServerStateProvider
    ): Server {
        val responseTimeFormatter = ResponseTimeFormatter(Locale.getDefault())
        return KtorServer(
            ApplicationEngineFactory(
                routeRegistrators = listOf(
                    LogRouteRegistrator(
                        getCallLogUseCase = getCallLogUseCase,
                        callLogResponseMapper = CallLogResponseMapper(responseTimeFormatter)
                    ),
                    RootRouteRegistrator(
                        responseTimeFormatter = responseTimeFormatter,
                        serverStateProvider = serverStateProvider
                    )
                )
            ),
            serverStateProvider = serverStateProvider
        )
    }
}
