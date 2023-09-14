package com.dd.callmonitor.ui.calllog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dd.callmonitor.presentation.calllog.CallLogViewModel

@Composable
fun ContentCallLogPermissionGranted(viewModel: CallLogViewModel) {

    val viewType = viewModel.viewType.collectAsStateWithLifecycle()

    when (viewType.value) {
        CallLogViewModel.ViewType.LOADING -> Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                Modifier
                    .width(52.dp)
                    .padding(bottom = 52.dp)
                    .align(Alignment.Center)
            )
        }

        CallLogViewModel.ViewType.CONTENT -> ContentCallLogLoaded(viewModel)

        CallLogViewModel.ViewType.FAILURE -> ContentCallLogFailure()
    }
}

@Preview
@Composable
fun ContentCallLogPermissionGrantedPreviewLoading() {
    ContentCallLogPermissionGranted(CallLogViewModel())
}
