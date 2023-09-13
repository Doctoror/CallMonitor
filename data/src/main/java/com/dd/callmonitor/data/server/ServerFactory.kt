package com.dd.callmonitor.data.server

import com.dd.callmonitor.data.server.routes.log.CallLogResponseMapper
import com.dd.callmonitor.data.server.routes.log.LogRouteRegistrator
import com.dd.callmonitor.data.server.routes.root.ResponseTimeFormatter
import com.dd.callmonitor.data.server.routes.root.RootRouteRegistrator
import com.dd.callmonitor.data.server.routes.status.StatusResponseMapper
import com.dd.callmonitor.data.server.routes.status.StatusRouteRegistrator
import com.dd.callmonitor.domain.calllog.GetCallLogUseCase
import com.dd.callmonitor.domain.callstatus.GetCallStatusUseCase
import com.dd.callmonitor.domain.contacts.TransformEmptyContactNameUseCase
import com.dd.callmonitor.domain.contacts.TransformEmptyPhoneNumberUseCase
import com.dd.callmonitor.domain.server.Server
import com.dd.callmonitor.domain.server.ServerStateProvider
import kotlinx.coroutines.CoroutineDispatcher
import java.util.Locale

/**
 * Note for reviewers: the goal is to encapsulate internal-keyword Server implementation in the data
 * module, and because data module does not have DI, a some kind of factory is necessary.
 */
class ServerFactory {

    fun newInstance(
        getCallLogUseCase: GetCallLogUseCase,
        getCallStatusUseCase: GetCallStatusUseCase,
        dispatcherIo: CoroutineDispatcher,
        locale: Locale,
        serverStateProvider: ServerStateProvider,
        transformEmptyContactNameUseCase: TransformEmptyContactNameUseCase,
        transformEmptyPhoneNumberUseCase: TransformEmptyPhoneNumberUseCase
    ): Server {
        val responseTimeFormatter = ResponseTimeFormatter(locale)
        return KtorServer(
            applicationEngineFactory = ApplicationEngineFactory(
                routeRegistrators = listOf(
                    LogRouteRegistrator(
                        getCallLogUseCase = getCallLogUseCase,
                        callLogResponseMapper = CallLogResponseMapper(
                            responseTimeFormatter = responseTimeFormatter,
                            transformEmptyContactNameUseCase = transformEmptyContactNameUseCase,
                            transformEmptyPhoneNumberUseCase = transformEmptyPhoneNumberUseCase
                        )
                    ),
                    StatusRouteRegistrator(
                        getCallStatusUseCase = getCallStatusUseCase,
                        statusResponseMapper = StatusResponseMapper()
                    )
                ).let { services ->
                    services + RootRouteRegistrator(
                        responseTimeFormatter = responseTimeFormatter,
                        serverStateProvider = serverStateProvider,
                        serviceNames = services.map { it.serviceName }
                    )
                }
            ),
            dispatcherIo = dispatcherIo,
            serverStateProvider = serverStateProvider
        )
    }
}
