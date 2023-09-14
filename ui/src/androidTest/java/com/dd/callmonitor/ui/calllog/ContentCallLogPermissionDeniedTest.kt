package com.dd.callmonitor.ui.calllog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.dd.callmonitor.presentation.calllog.CallLogViewModel
import com.dd.callmonitor.ui.R
import com.dd.callmonitor.ui.testutils.PermissionDeniedTestActivity
import com.dd.callmonitor.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class ContentCallLogPermissionDeniedTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val composeTestRule = createAndroidComposeRule<PermissionDeniedTestActivity>()

    @Test
    fun showsViewTypePermissionDenied() {
        composeTestRule.setContent {
            AppTheme {
                ContentCallLog(
                    viewModel = CallLogViewModel(),
                    onApplicationSettingsClick = {},
                    onReadCallLogPermissionGranted = {},
                    shouldAutoAskForPermissions = false
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.call_log_permission_denied_icon))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.call_log_permission_rationale_text))
            .assertIsDisplayed()
    }
}
