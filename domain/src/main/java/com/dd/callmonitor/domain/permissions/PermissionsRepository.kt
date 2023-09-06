package com.dd.callmonitor.domain.permissions

interface PermissionsRepository {

    suspend fun setPermissionRationaleShown(permission: String, shown: Boolean)

    suspend fun wasPermissionRationaleShown(permission: String): Boolean
}
