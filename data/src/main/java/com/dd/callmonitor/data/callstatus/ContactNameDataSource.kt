package com.dd.callmonitor.data.callstatus

import android.content.ContentResolver
import android.provider.ContactsContract
import com.dd.callmonitor.domain.permissions.ApiLevelPermissions
import com.dd.callmonitor.domain.permissions.CheckPermissionUseCase
import com.dd.callmonitor.domain.phonenumbers.NormalizePhoneNumberUseCase
import com.dd.callmonitor.domain.util.Optional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ContactNameDataSource(
    private val checkPermissionUseCase: CheckPermissionUseCase,
    private val contentResolver: ContentResolver,
    private val normalizePhoneNumberUseCase: NormalizePhoneNumberUseCase
) {

    suspend fun getContactNameByPhoneNumber(phoneNumber: String): Optional<String> =
        if (phoneNumber.isBlank()) {
            Optional.empty()
        } else {
            checkPermissionUseCase(
                permission = ApiLevelPermissions.READ_CONTACTS,
                // Of course, we could handle it differently, like signalling an error, but I
                // decided to just return empty here
                whenDenied = { Optional.empty() },
                whenGranted = {
                    withContext(Dispatchers.IO) {
                        getContactIdByPhoneNumber(phoneNumber)
                            .flatMap(::getContactNameByContactId)
                    }
                }
            )
        }

    private fun getContactIdByPhoneNumber(phoneNumber: String): Optional<String> =
        contentResolver
            .query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID),
                "${ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER} = ?",
                arrayOf(normalizePhoneNumberUseCase(phoneNumber)),
                null
            )
            .use {
                if (it?.moveToFirst() == true) {

                    val id = it.getString(
                        it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                    )

                    if (id != null) {
                        return@use Optional.of(id)
                    }
                }

                return@use Optional.empty()
            }

    private fun getContactNameByContactId(contactId: String): Optional<String> =
        contentResolver
            .query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
                "${ContactsContract.Contacts._ID} = ?",
                arrayOf(contactId),
                null
            )
            .use {
                if (it?.moveToFirst() == true) {
                    Optional.ofNullable(
                        it.getString(
                            it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
                        )
                    )
                } else {
                    Optional.empty()
                }
            }
}
