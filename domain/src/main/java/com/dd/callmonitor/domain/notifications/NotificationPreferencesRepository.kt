package com.dd.callmonitor.domain.notifications

interface NotificationPreferencesRepository {

    suspend fun setPermissionRationaleShown(shown: Boolean)

    suspend fun wasPermissionRationaleShown(): Boolean
}
