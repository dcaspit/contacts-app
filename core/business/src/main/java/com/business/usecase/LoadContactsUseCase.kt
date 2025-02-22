package com.business.usecase

import com.business.model.Contact
import com.business.repository.ContactsRepository
import com.common.state.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadContactsUseCase @Inject constructor(private val repository: ContactsRepository) {
    operator fun invoke(): Flow<DataState<List<Contact>>> = repository.loadContacts()
}