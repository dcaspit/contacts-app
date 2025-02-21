package com.feature.home.business.repository

import com.common.state.DataState
import com.feature.home.business.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContacts(): Flow<DataState<List<Contact>>>
}