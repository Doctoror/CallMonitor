package com.dd.callmonitor.presentation.calllog

import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.calllog.CallLogError
import com.dd.callmonitor.domain.util.ResultOrFailure

class CallLogViewModelUpdater(
    private val callLogEntryViewModelMapper: CallLogEntryViewModelMapper
) {

    fun updateOnCallLogLoaded(
        viewModel: CallLogViewModel,
        callLogResult: ResultOrFailure<List<CallLogEntry>, CallLogError>
    ) {
        callLogResult.fold(
            onSuccess = {
                viewModel.callLog.value = it.map(callLogEntryViewModelMapper::map)
                viewModel.viewType.value = CallLogViewModel.ViewType.CONTENT
            },
            onFailure = {
                viewModel.viewType.value = CallLogViewModel.ViewType.FAILURE
            }
        )
    }
}
