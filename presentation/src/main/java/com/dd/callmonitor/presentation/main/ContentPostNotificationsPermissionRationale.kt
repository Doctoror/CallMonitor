package com.dd.callmonitor.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dd.callmonitor.presentation.R
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ContentPostNotificationsPermissionRationale(
    onPostNotificationsPermissionRationaleDismissRequest: () -> Unit,
    onPostNotificationsPermissionRationaleProceed: () -> Unit,
    showPostNotificationsPermissionRationaleFlow: StateFlow<Boolean>
) {

    val showPostNotificationsPermissionRationale =
        showPostNotificationsPermissionRationaleFlow.collectAsStateWithLifecycle()

    if (showPostNotificationsPermissionRationale.value) {
        ContentPermissionRationaleDialog(
            onDismissRequest = onPostNotificationsPermissionRationaleDismissRequest,
            onProceed = onPostNotificationsPermissionRationaleProceed,
            text = stringResource(R.string.dialog_notification_permission_rationale_text)
        )
    }
}
