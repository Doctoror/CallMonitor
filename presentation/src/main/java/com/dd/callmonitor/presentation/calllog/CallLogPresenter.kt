package com.dd.callmonitor.presentation.calllog

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dd.callmonitor.domain.calllog.ObserveCallLogUseCase
import kotlinx.coroutines.launch

class CallLogPresenter(
    private val callLogViewModelUpdater: CallLogViewModelUpdater,
    private val observeCallLogUseCase: ObserveCallLogUseCase,
    val viewModel: CallLogViewModel
) : ViewModel() {

    private var initialized = false

    @MainThread
    fun onReadCallLogPermissionGranted() {
        if (!initialized) {
            initialized = true

            viewModelScope.launch {
                observeCallLogUseCase()
                    .collect { callLogViewModelUpdater.updateOnCallLogLoaded(viewModel, it) }
            }
        }
    }
}
