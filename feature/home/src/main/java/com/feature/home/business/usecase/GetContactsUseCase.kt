package com.feature.home.business.usecase

import com.common.state.DataState
import com.feature.home.business.model.Contact
import com.feature.home.business.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(private val repository: ContactsRepository) {
    operator fun invoke(): Flow<DataState<List<Contact>>> = repository.getContacts()
}