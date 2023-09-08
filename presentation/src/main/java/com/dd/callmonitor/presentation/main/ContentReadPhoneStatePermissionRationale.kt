package com.dd.callmonitor.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dd.callmonitor.presentation.R
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ContentReadPhoneNumbersPermissionRationale(
    onReadPhoneNumbersPermissionRationaleDismissRequest: () -> Unit,
    onReadPhoneNumbersPermissionRationaleProceed: () -> Unit,
    showReadPhoneNumbersPermissionRationaleFlow: StateFlow<Boolean>
) {

    val showReadPhoneNumbersPermissionRationale =
        showReadPhoneNumbersPermissionRationaleFlow.collectAsStateWithLifecycle()

    if (showReadPhoneNumbersPermissionRationale.value) {
        ContentPermissionRationaleDialog(
            onDismissRequest = onReadPhoneNumbersPermissionRationaleDismissRequest,
            onProceed = onReadPhoneNumbersPermissionRationaleProceed,
            text = stringResource(R.string.dialog_read_phone_numbers_rationale_text)
        )
    }
}
