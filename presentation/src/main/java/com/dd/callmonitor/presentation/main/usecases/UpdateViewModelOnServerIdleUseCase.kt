package com.dd.callmonitor.presentation.main.usecases

import android.content.res.Resources
import androidx.compose.ui.graphics.Color
import com.dd.callmonitor.presentation.R
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsViewModel

class UpdateViewModelOnServerIdleUseCase(private val resources: Resources) {

    operator fun invoke(viewModel: ServerControlsViewModel) {
        viewModel.powerButtonAction.value = ServerControlsViewModel.PowerButtonAction.START

        viewModel.powerButtonContentDescription.value = resources.getString(
            R.string.server_power_button_when_idle_content_description
        )

        viewModel.powerButtonLabel.value = resources.getString(
            R.string.server_power_button_when_idle_label
        )

        viewModel.powerButtonLoading.value = false

        viewModel.powerButtonTint.value = Color.Red

        viewModel.viewType.value = ServerControlsViewModel.ViewType.CONTENT
    }
}
