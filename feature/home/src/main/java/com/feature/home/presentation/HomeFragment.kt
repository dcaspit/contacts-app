package com.feature.home.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.feature.home.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

private const val CONTACTS_PERMISSION_REQUEST = 1

private const val CONTACTS_PERMISSION = "android.permission.READ_CONTACTS"

class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    val adapter = ContactListAdapter(object : ContactListAdapter.OnClickListener {
        override fun onClickContact(contact: Contact) {

        }

        override fun onClickImage(contact: Contact) {

        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)



        binding.contactList.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestContactsPermissionOrStartLoadingList()
        contactList.observe(viewLifecycleOwner) { list ->
            Log.d("Daniel", "$list")
            binding.contactList.addItemDecoration(DividerItemDecoration(binding.root.context, LinearLayoutManager.VERTICAL))
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val _contactList = MutableLiveData<List<Contact>>()
    /**
     * A list of contacts that can be shown on the screen.
     * Views should use this to get access to the data.
     */
    val contactList: LiveData<List<Contact>>
        get() = _contactList

    suspend fun getContacts() {
        withContext(Dispatchers.IO) {

            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            )

            val contacts = mutableListOf<Contact>()

            val cursor = requireContext().contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC"
            )

            if ((cursor?.count ?: 0) > 0) {

                val normalizedNumbersAlreadyFound = hashSetOf<String>()
                val indexOfNormalizedNumber =
                    cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
                val indexOfDisplayName = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)
                val indexOfDisplayNumber = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val indexOfPhotoUri = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                val indexOfContactID = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

                while (cursor!!.moveToNext()) {
                    val normalizedNumber = cursor.getString(indexOfNormalizedNumber!!)
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        val name = cursor.getString(indexOfDisplayName!!)
                        val phoneNumber =
                            cursor.getString(indexOfDisplayNumber!!)
                        val photoUri =
                            cursor.getString(indexOfPhotoUri!!)
                        val id =
                            cursor.getString(indexOfContactID!!)

                        Log.d("contact", "getAllContacts: $name $phoneNumber $photoUri")
                        val contact = Contact(id, name, phoneNumber, photoUri)

                        contacts.add(contact)
                    }
                }
            }
            cursor?.close()

            _contactList.postValue(contacts)
        }
    }

    private fun requestContactsPermissionOrStartLoadingList() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                CONTACTS_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestContactSPermission()
            return
        }
        runBlocking {
            getContacts()
        }
    }

    /**
     * Show the user a dialog asking for permission to show contacts.
     */
    private fun requestContactSPermission() {
        requestPermissions(arrayOf(CONTACTS_PERMISSION), CONTACTS_PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONTACTS_PERMISSION_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    runBlocking {
                        getContacts()
                    }
                } else {
                    Toast.makeText(
                        activity, "bla bla",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }
}

data class Contact(
    val id: String,
    val name: String,
    val number: String,
    val imageUri: String?
)