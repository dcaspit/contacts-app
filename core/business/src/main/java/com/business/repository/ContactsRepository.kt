package com.business.repository

import com.business.model.Contact
import com.common.state.DataState
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun loadContacts(): Flow<DataState<List<Contact>>>
}