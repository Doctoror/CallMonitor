package com.dd.callmonitor.presentation.server.externalcontrols.updaters

import android.content.res.Resources
import android.graphics.Color
import com.dd.callmonitor.domain.connectivity.FormatHostAndPortUseCase
import com.dd.callmonitor.presentation.R
import com.dd.callmonitor.presentation.server.externalcontrols.ServerControlsViewModel

class ServerRunningViewModelUpdater(
    private val formatHostAndPortUseCase: FormatHostAndPortUseCase,
    private val resources: Resources
) {

    operator fun invoke(viewModel: ServerControlsViewModel, ip: String, port: Int) {
        viewModel.powerButtonAction.value = ServerControlsViewModel.PowerButtonAction.STOP

        viewModel.powerButtonContentDescription.value = resources.getString(
            R.string.server_power_button_when_running_content_description
        )

        viewModel.powerButtonLabel.value = resources.getString(
            R.string.server_power_button_when_running_label, formatHostAndPortUseCase(ip, port)
        )

        viewModel.powerButtonLoading.value = false

        viewModel.powerButtonTint.value = Color.GREEN

        viewModel.viewType.value = ServerControlsViewModel.ViewType.CONTENT
    }
}
