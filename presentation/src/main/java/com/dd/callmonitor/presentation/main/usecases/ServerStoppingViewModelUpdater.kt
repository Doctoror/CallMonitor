package com.dd.callmonitor.presentation.main.usecases

import android.content.res.Resources
import com.dd.callmonitor.presentation.R
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsViewModel

class ServerStoppingViewModelUpdater(private val resources: Resources) {

    operator fun invoke(viewModel: ServerControlsViewModel) {
        viewModel.powerButtonAction.value = ServerControlsViewModel.PowerButtonAction.NONE

        viewModel.powerButtonLabel.value = resources.getString(
            R.string.server_power_button_when_stopping_label
        )

        viewModel.powerButtonLoading.value = true

        viewModel.viewType.value = ServerControlsViewModel.ViewType.CONTENT
    }
}
