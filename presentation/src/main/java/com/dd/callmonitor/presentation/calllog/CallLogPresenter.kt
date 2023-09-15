package com.dd.callmonitor.presentation.calllog

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dd.callmonitor.domain.calllog.ObserveCallLogUseCase
import kotlinx.coroutines.launch

class CallLogPresenter(
    private val callLogViewModelUpdater: CallLogViewModelUpdater,
    private val observeCallLogUseCase: ObserveCallLogUseCase,
    // Note for reviewers: arguably, having the property below as public is a Law of Demeter
    // violation, however, this accessor will be used only once and having a viewModel here provides
    // more benefits like ensuring the same instance for a presenter
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
