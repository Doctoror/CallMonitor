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

    fun onReadCallLogPermissionGranted() {
        viewModelScope.launch {
            // TODO this will not refresh until you close the screen
            callLogViewModelUpdater.updateOnCallLogLoaded(viewModel, getCallLogUseCase())
        }
    }
}
