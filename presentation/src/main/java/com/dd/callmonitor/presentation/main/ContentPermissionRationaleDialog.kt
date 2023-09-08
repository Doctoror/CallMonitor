package com.dd.callmonitor.presentation.main

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dd.callmonitor.presentation.R

@Composable
fun ContentPermissionRationaleDialog(
    onDismissRequest: () -> Unit,
    onProceed: () -> Unit,
    text: String
) {
    AlertDialog(
        text = { Text(text) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onProceed) {
                Text(stringResource(R.string.permission_rationale_proceed))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.dialog_permission_rationale_dismiss))
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun ContentPermissionRationaleDialogPreview() {
    ContentPermissionRationaleDialog(
        onDismissRequest = {},
        onProceed = {},
        text = stringResource(R.string.call_log_permission_rationale_text)
    )
}
