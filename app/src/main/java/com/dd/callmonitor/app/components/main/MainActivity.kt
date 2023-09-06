package com.dd.callmonitor.app.components.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.dd.callmonitor.domain.permissions.RequestCallLogPermissionUseCase
import com.dd.callmonitor.domain.permissions.RequestPostNotificationsPermissionUseCase
import com.dd.callmonitor.presentation.main.ContentMain
import com.dd.callmonitor.presentation.main.MainPresenter
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    private val requestCallLogPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            presenter.onCallLogPermissionGranted()
        } else {
            presenter.onCallLogPermissionDenied()
        }
    }

    private val requestPostNotificationsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            presenter.onPostNotificationsPermissionGranted()
        } else {
            presenter.onPostNotificationsPermissionDenied()
        }
    }

    private val presenter: MainPresenter by inject {
        parametersOf(
            this,
            RequestCallLogPermissionUseCase(requestCallLogPermissionLauncher),
            RequestPostNotificationsPermissionUseCase(requestPostNotificationsPermissionLauncher)
        )
    }

    private val viewModel by lazy { presenter.viewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentMain(
                onCallLogPermissionRationaleDismiss = presenter::onCallLogPermissionRationaleDismiss,
                onCallLogPermissionRationaleProceed = presenter::onCallLogPermissionRationaleProceed,
                onPostNotificationsPermissionRationaleDismiss = presenter::onPostNotificationsPermissionRationaleDismiss,
                onPostNotificationsPermissionRationaleProceed = presenter::onPostNotificationsPermissionRationaleProceed,
                onStartServerClick = presenter::onStartServerClick,
                onStopServerClick = presenter::onStopServerClick,
                viewModel = viewModel
            )
        }

        presenter.onCreate(this)
    }
}
