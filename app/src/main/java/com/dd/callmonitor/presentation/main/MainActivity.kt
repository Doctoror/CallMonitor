package com.dd.callmonitor.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

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
        parametersOf(requestPostNotificationsPermissionLauncher)
    }

    private val viewModel by lazy { presenter.viewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentMain(
                onPermissionRationaleDismiss = presenter::onPostNotificationsPermissionRationaleDismiss,
                onPermissionRationaleProceed = presenter::onPostNotificationsPermissionRationaleProceed,
                onStartServerClick = { presenter.onStartServerClick(this) },
                onStopServerClick = presenter::onStopServerClick,
                viewModel = viewModel
            )
        }
        lifecycle.addObserver(presenter)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(presenter)
    }
}
