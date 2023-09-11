package com.dd.callmonitor.presentation.calllog

import com.dd.callmonitor.domain.calllog.CallLogEntry
import com.dd.callmonitor.domain.calllog.CallLogError
import com.dd.callmonitor.domain.util.Either

class CallLogViewModelUpdater(
    private val callLogEntryViewModelMapper: CallLogEntryViewModelMapper
) {

    fun updateOnCallLogLoaded(
        viewModel: CallLogViewModel,
        callLogResult: Either<CallLogError, List<CallLogEntry>>
    ) {
        callLogResult.fold(
            onLeft = {
                viewModel.viewType.value = CallLogViewModel.ViewType.FAILURE
            },
            onRight = {
                viewModel.callLog.value = it.map(callLogEntryViewModelMapper::map)
                viewModel.viewType.value = CallLogViewModel.ViewType.CONTENT
            }
        )
    }
}
