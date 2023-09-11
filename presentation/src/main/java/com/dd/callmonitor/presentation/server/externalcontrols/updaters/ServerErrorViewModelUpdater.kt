package com.dd.callmonitor.presentation.server.externalcontrols.updaters

import android.content.res.Resources
import androidx.compose.ui.graphics.Color
import com.dd.callmonitor.domain.server.ServerError
import com.dd.callmonitor.presentation.R
import com.dd.callmonitor.presentation.server.externalcontrols.ServerControlsViewModel

class ServerErrorViewModelUpdater(private val resources: Resources) {

    operator fun invoke(viewModel: ServerControlsViewModel, error: ServerError) {
        viewModel.powerButtonAction.value = ServerControlsViewModel.PowerButtonAction.START

        viewModel.powerButtonContentDescription.value = resources.getString(
            R.string.server_power_button_when_idle_content_description
        )

        viewModel.powerButtonLabel.value = resources.getString(
            when (error) {
                ServerError.GENERIC ->
                    R.string.server_power_button_when_error_label_generic

                ServerError.NO_CONNECTIVITY ->
                    R.string.server_power_button_when_error_label_not_connected

                ServerError.NO_HOST_ADDRESS ->
                    R.string.server_power_button_when_error_label_no_host_address
            }
        )

        viewModel.powerButtonLoading.value = false

        viewModel.powerButtonTint.value = Color.Red

        viewModel.viewType.value = ServerControlsViewModel.ViewType.CONTENT
    }
}
