package com.dd.callmonitor.presentation.server

import android.content.res.Resources
import com.dd.callmonitor.domain.server.ServerError
import com.dd.callmonitor.presentation.R

class ServerErrorNotificationMessageProvider(private val resources: Resources) {

    fun provide(error: ServerError): CharSequence = resources.getText(
        when (error) {
            ServerError.GENERIC ->
                R.string.server_power_button_when_error_label_generic

            ServerError.NO_CONNECTIVITY ->
                R.string.server_power_button_when_error_label_not_connected

            ServerError.NO_HOST_ADDRESS ->
                R.string.server_power_button_when_error_label_no_host_address
        }
    )
}
