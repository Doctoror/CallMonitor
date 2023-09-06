package com.dd.callmonitor.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.ContentPowerButtonWithLabel(
    isLoading: Boolean,
    label: String,
    tint: Color,
    onClick: (() -> Unit)?,
    onClickLabel: String?
) {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(bottom = 56.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(11.dp)
                    .size(66.dp)
            )
        } else {
            var modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(88.dp)

            if (onClick != null) {
                modifier = modifier.clickable(
                    onClick = onClick,
                    onClickLabel = onClickLabel
                )
            }

            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = onClickLabel,
                modifier = modifier,
                tint = tint,
            )
        }

        Text(
            text = label,
            minLines = 2,
            textAlign = TextAlign.Center
        )
    }
}
