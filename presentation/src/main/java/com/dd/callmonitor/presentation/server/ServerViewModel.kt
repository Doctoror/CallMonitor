package com.dd.callmonitor.presentation.server

import kotlinx.coroutines.flow.MutableSharedFlow

class ServerViewModel {

    val foregroundNotification = MutableSharedFlow<CharSequence>()

    val normalNotification = MutableSharedFlow<CharSequence>()
}
