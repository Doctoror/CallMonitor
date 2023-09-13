package com.dd.callmonitor.presentation.calllog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dd.callmonitor.domain.calllog.GetCallLogUseCase
import kotlinx.coroutines.launch

class CallLogPresenter(
    private val callLogViewModelUpdater: CallLogViewModelUpdater,
    private val getCallLogUseCase: GetCallLogUseCase,
    val viewModel: CallLogViewModel
) : ViewModel() {

    private var initialized = false

    fun onReadCallLogPermissionGranted() {
        if (!initialized) {
            initialized = true
            viewModelScope.launch {
                // TODO this will not refresh until destroyed
                callLogViewModelUpdater.updateOnCallLogLoaded(viewModel, getCallLogUseCase())
            }
        }
    }
}
