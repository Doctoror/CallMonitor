package com.dd.callmonitor.presentation.main.usecases

import android.content.res.Resources
import androidx.compose.ui.graphics.Color
import com.dd.callmonitor.R
import com.dd.callmonitor.presentation.main.MainViewModel

class UpdateViewModelOnServerErrorUseCase(private val resources: Resources) {

    operator fun invoke(viewModel: MainViewModel) {
        viewModel.powerButtonAction.value = MainViewModel.PowerButtonAction.START

        viewModel.powerButtonContentDescription.value = resources.getString(
            R.string.server_power_button_when_idle_content_description
        )

        viewModel.powerButtonLabel.value = resources.getString(
            R.string.server_power_button_when_error_label
        )

        viewModel.powerButtonLoading.value = false

        viewModel.powerButtonTint.value = Color.Red

        viewModel.viewType.value = MainViewModel.ViewType.SERVER_CONTROLS
    }
}
