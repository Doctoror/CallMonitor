package com.dd.callmonitor.presentation.main.servercontrol

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BoxScope.ContentServerControls(
    onStartServerClick: () -> Unit,
    onStopServerClick: () -> Unit,
    viewModel: ServerControlsViewModel
) {
    val powerButtonAction = viewModel.powerButtonAction
        .collectAsStateWithLifecycle()

    val powerButtonContentDescription = viewModel.powerButtonContentDescription
        .collectAsStateWithLifecycle()

    val powerButtonLabel = viewModel.powerButtonLabel
        .collectAsStateWithLifecycle()

    val powerButtonLoading = viewModel.powerButtonLoading
        .collectAsStateWithLifecycle()

    val powerButtonTint = viewModel.powerButtonTint
        .collectAsStateWithLifecycle()

    val viewType = viewModel.viewType
        .collectAsStateWithLifecycle()

    when (viewType.value) {
        ServerControlsViewModel.ViewType.LOADING -> CircularProgressIndicator(
            Modifier
                .width(52.dp)
                .padding(bottom = 52.dp)
                .align(Alignment.Center)
        )

        ServerControlsViewModel.ViewType.NO_CONNECTION -> ContentServerControlNoConnection()

        ServerControlsViewModel.ViewType.CONTENT -> ContentPowerButtonWithLabel(
            isLoading = powerButtonLoading.value,
            label = powerButtonLabel.value,
            tint = powerButtonTint.value,
            onClick = when (powerButtonAction.value) {
                ServerControlsViewModel.PowerButtonAction.NONE -> null
                ServerControlsViewModel.PowerButtonAction.START -> onStartServerClick
                ServerControlsViewModel.PowerButtonAction.STOP -> onStopServerClick
            },
            onClickLabel = powerButtonContentDescription.value
        )
    }
}
