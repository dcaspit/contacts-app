package com.feature.home.data.repository

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.common.state.DataState
import com.feature.home.business.model.Contact
import com.feature.home.business.repository.ContactsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ContactsRepository {

    override fun getContacts() = flow<DataState<List<Contact>>> {
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        )

        val contacts = mutableListOf<Contact>()

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC"
        )

        if ((cursor?.count ?: 0) > 0) {

            val normalizedNumbersAlreadyFound = hashSetOf<String>()
            val indexOfNormalizedNumber =
                cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
            val indexOfDisplayName =
                cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)
            val indexOfDisplayNumber =
                cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val indexOfPhotoUri =
                cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
            val indexOfContactID =
                cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

            while (cursor!!.moveToNext()) {
                val normalizedNumber = cursor.getString(indexOfNormalizedNumber!!)
                if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                    val name = cursor.getString(indexOfDisplayName!!)
                    val phoneNumber = cursor.getString(indexOfDisplayNumber!!)
                    val photoUri = cursor.getString(indexOfPhotoUri!!)
                    val id = cursor.getString(indexOfContactID!!)

                    Log.d("contact", "getAllContacts: $name $phoneNumber $photoUri")
                    val contact = Contact(id, name, phoneNumber, photoUri)

                    contacts.add(contact)
                }
            }
        }
        cursor?.close()

        emit(DataState.Success<List<Contact>>(contacts))
    }
}