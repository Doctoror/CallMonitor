package com.dd.callmonitor.presentation.main

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dd.callmonitor.R

@Composable
fun ContentNotificationPermissionRationaleDialog(
    onDismiss: () -> Unit,
    onProceed: () -> Unit
) {
    AlertDialog(
        text = {
            Text(text = stringResource(R.string.dialog_notification_permission_rationale_text))
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onProceed) {
                Text(stringResource(R.string.dialog_notification_permission_rationale_proceed))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_notification_permission_rationale_dismiss))
            }
        }
    )
}
