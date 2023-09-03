package com.dd.callmonitor.domain.notifications

import androidx.fragment.app.FragmentActivity
import com.dd.callmonitor.presentation.main.NotificationPermissionRationaleDialogFragment

class ShowNotificationPermissionRationaleUseCase {

    operator fun invoke(activity: FragmentActivity) {
        activity
            .supportFragmentManager
            .beginTransaction()
            .show(NotificationPermissionRationaleDialogFragment())
            .commitAllowingStateLoss()
    }
}
