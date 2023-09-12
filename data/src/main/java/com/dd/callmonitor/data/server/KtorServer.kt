package com.dd.callmonitor.data.server

import android.util.Log
import com.dd.callmonitor.domain.server.Server
import com.dd.callmonitor.domain.server.ServerError
import com.dd.callmonitor.domain.server.ServerState
import com.dd.callmonitor.domain.server.ServerStateProvider
import io.ktor.server.engine.ApplicationEngine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.net.InetAddress
import kotlin.coroutines.cancellation.CancellationException

internal class KtorServer(
    private val applicationEngineFactory: ApplicationEngineFactory,
    private val dispatcherIo: CoroutineDispatcher,
    private val serverStateProvider: ServerStateProvider
) : Server {

    private var engine: ApplicationEngine? = null

    private val logTag by lazy { "KtorNettyServer" }

    private val mutex = Mutex()

    override suspend fun start(host: InetAddress) = withContext(dispatcherIo) {
        mutex.withLock {
            serverStateProvider.state.emit(ServerState.Initialising)
            if (engine != null) {
                throw IllegalStateException("Previous engine instance not destroyed")
            }

            val hostAddress = host.hostAddress
            if (hostAddress == null) {
                serverStateProvider.state.emit(ServerState.Error(ServerError.NO_HOST_ADDRESS))
            } else {
                try {
                    engine = applicationEngineFactory.newInstance(hostAddress, PORT)
                    engine!!.start()
                    serverStateProvider.state.emit(
                        ServerState.Running(
                            hostAddress,
                            PORT,
                            System.currentTimeMillis()
                        )
                    )
                } catch (e: Exception) {
                    Log.w(logTag, "Failed to make or start engine", e)
                    engine = null

                    serverStateProvider.state.emit(ServerState.Error(ServerError.GENERIC))
                    if (e is CancellationException) {
                        throw e
                    }
                }
            }
        }
    }

    override suspend fun stopIfRunning() = withContext(dispatcherIo) {
        mutex.withLock {
            if (engine != null) {
                serverStateProvider.state.emit(ServerState.Stopping)
                try {
                    engine!!.stop()
                } catch (e: Exception) {
                    Log.w(logTag, "Failed to stop engine", e)
                    throw e
                } finally {
                    engine = null
                }
                serverStateProvider.state.emit(ServerState.Idle)
            }
        }
    }
}
