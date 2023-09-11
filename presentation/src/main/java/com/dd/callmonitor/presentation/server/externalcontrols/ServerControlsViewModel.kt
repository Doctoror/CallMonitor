package com.dd.callmonitor.presentation.server.externalcontrols

import android.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow

class ServerControlsViewModel {

    val powerButtonAction = MutableStateFlow(PowerButtonAction.NONE)
    val powerButtonContentDescription = MutableStateFlow("")
    val powerButtonLabel = MutableStateFlow("")
    val powerButtonLoading = MutableStateFlow(false)
    val powerButtonTint = MutableStateFlow(Color.BLACK)
    val viewType = MutableStateFlow(ViewType.LOADING)

    enum class PowerButtonAction {
        NONE,
        START,
        STOP
    }

    enum class ViewType {

        LOADING,
        NO_CONNECTION,
        CONTENT,
    }
}
