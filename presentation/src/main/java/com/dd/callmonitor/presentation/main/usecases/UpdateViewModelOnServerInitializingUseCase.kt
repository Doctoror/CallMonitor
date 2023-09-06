package com.dd.callmonitor.presentation.main.usecases

import android.content.res.Resources
import com.dd.callmonitor.presentation.R
import com.dd.callmonitor.presentation.main.MainViewModel

class UpdateViewModelOnServerInitializingUseCase(private val resources: Resources) {

    operator fun invoke(viewModel: MainViewModel) {
        viewModel.powerButtonAction.value = MainViewModel.PowerButtonAction.NONE

        viewModel.powerButtonLabel.value = resources.getString(
            R.string.server_power_button_when_initializing_label
        )

        viewModel.powerButtonLoading.value = true

        viewModel.viewType.value = MainViewModel.ViewType.SERVER_CONTROLS
    }
}
