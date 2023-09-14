package com.dd.callmonitor.presentation.server

import android.content.res.Resources
import com.dd.callmonitor.domain.connectivity.FormatHostAndPortUseCase
import com.dd.callmonitor.domain.server.ServerState
import com.dd.callmonitor.domain.util.Optional
import com.dd.callmonitor.presentation.R

class ForegroundServiceStatusMessageProvider(
    private val formatHostAndPortUseCase: FormatHostAndPortUseCase,
    private val resources: Resources
) {

    /**
     * @return status message for handled states. If the state is not applicable for a foreground
     * service, [Optional.empty] is returned.
     */
    operator fun invoke(state: ServerState): Optional<CharSequence> =
        when (state) {
            is ServerState.Initialising ->
                Optional.of(resources.getText(R.string.server_power_button_when_initializing_label))

            is ServerState.Running ->
                Optional.of(
                    resources.getString(
                        R.string.server_power_button_when_running_label,
                        formatHostAndPortUseCase(state.host, state.port)
                    )
                )

            is ServerState.Stopping ->
                Optional.of(resources.getText(R.string.server_power_button_when_stopping_label))

            is ServerState.Error,
            is ServerState.Idle -> Optional.empty()
        }
}
