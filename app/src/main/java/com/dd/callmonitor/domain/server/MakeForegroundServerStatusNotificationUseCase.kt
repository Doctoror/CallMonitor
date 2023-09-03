package com.dd.callmonitor.domain.server

import android.app.Notification
import android.content.Context
import com.dd.callmonitor.R
import com.dd.callmonitor.data.server.ServerState
import com.dd.callmonitor.domain.FormatIpAndPortUseCase
import com.dd.callmonitor.framework.Optional

class MakeForegroundServerStatusNotificationUseCase(
    private val formatIpAndPortUseCase: FormatIpAndPortUseCase,
    private val makeServerStatusNotificationUseCase: MakeServerStatusNotificationUseCase
) {

    /**
     * @return a [Notification] for foreground service, or Optional.empty if the [ServerState] is
     * not applicable for going foreground.
     */
    operator fun invoke(context: Context, state: ServerState): Optional<Notification> {
        val statusMessage = getStatusMessage(context, state) ?: return Optional.empty()

        return Optional.of(
            makeServerStatusNotificationUseCase(context, statusMessage)
        )
    }

    /**
     * @return status message for handled states. If the state is not applicable for a foreground
     * service, null is returned.
     */
    private fun getStatusMessage(context: Context, state: ServerState): CharSequence? =
        when (state) {
            is ServerState.Initialising ->
                context.getText(R.string.server_power_button_when_initializing_label)

            is ServerState.Running ->
                context.getString(
                    R.string.server_power_button_when_running_label,
                    formatIpAndPortUseCase.format(state.ip, state.port)
                )

            is ServerState.Stopping ->
                context.getText(R.string.server_power_button_when_stopping_label)

            is ServerState.Error,
            is ServerState.Idle -> null
        }
}
