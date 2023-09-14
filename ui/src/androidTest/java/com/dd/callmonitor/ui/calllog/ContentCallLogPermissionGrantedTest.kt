package com.dd.callmonitor.ui.calllog

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.dd.callmonitor.presentation.calllog.CallLogEntryViewModel
import com.dd.callmonitor.presentation.calllog.CallLogViewModel
import com.dd.callmonitor.ui.R
import com.dd.callmonitor.ui.testutils.PermissionGrantedTestActivity
import com.dd.callmonitor.ui.theme.AppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ContentCallLogPermissionGrantedTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val composeTestRule = createAndroidComposeRule<PermissionGrantedTestActivity>()

    @Test
    fun invokesOnReadCallLogPermissionGranted() {
        var onReadCallLogPermissionGrantedCalled = false

        composeTestRule.setContent {
            AppTheme {
                ContentCallLog(
                    viewModel = CallLogViewModel(),
                    onReadCallLogPermissionGranted = {
                        onReadCallLogPermissionGrantedCalled = true
                    },
                    shouldAutoAskForPermissions = false
                )
            }
        }

        assertTrue(onReadCallLogPermissionGrantedCalled)
    }

    @Test
    fun showsViewTypeLoading() {
        composeTestRule.setContent {
            AppTheme {
                ContentCallLog(
                    viewModel = CallLogViewModel(),
                    onReadCallLogPermissionGranted = {},
                    shouldAutoAskForPermissions = false
                )
            }
        }

        composeTestRule
            .onNode(
                SemanticsMatcher(
                    "ProgressSemanticsMatcher"
                ) {
                    it.config.contains(SemanticsProperties.ProgressBarRangeInfo)
                }
            )
            .assertIsDisplayed()
    }

    @Test
    fun showsViewTypeFailure() {
        composeTestRule.setContent {
            AppTheme {
                ContentCallLog(
                    viewModel = CallLogViewModel()
                        .apply { viewType.value = CallLogViewModel.ViewType.FAILURE },
                    onReadCallLogPermissionGranted = {},
                    shouldAutoAskForPermissions = false
                )
            }
        }

        composeTestRule
            .onNodeWithTag(TEST_TAG_CALL_LOG_FAILURE_ICON)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.call_log_failure))
            .assertIsDisplayed()
    }

    @Test
    fun showsViewTypeContentEmpty() {
        composeTestRule.setContent {
            AppTheme {
                ContentCallLog(
                    viewModel = CallLogViewModel()
                        .apply { viewType.value = CallLogViewModel.ViewType.CONTENT },
                    onReadCallLogPermissionGranted = {},
                    shouldAutoAskForPermissions = false
                )
            }
        }

        composeTestRule
            .onNodeWithTag(TEST_TAG_CALL_LOG_EMPTY_ICON)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.call_log_empty))
            .assertIsDisplayed()
    }

    @Test
    fun showsViewTypeContent() {
        val callLogEntries = listOf(
            CallLogEntryViewModel(
                duration = "00:03",
                name = "Uncle Bob"
            )
        )

        composeTestRule.setContent {
            AppTheme {
                ContentCallLog(
                    viewModel = CallLogViewModel().apply {
                        callLog.value = callLogEntries
                        viewType.value = CallLogViewModel.ViewType.CONTENT
                    },
                    onReadCallLogPermissionGranted = {},
                    shouldAutoAskForPermissions = false
                )
            }
        }

        callLogEntries.forEach {
            composeTestRule
                .onNodeWithText(it.name)
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText(it.duration)
                .assertIsDisplayed()
        }
    }
}

