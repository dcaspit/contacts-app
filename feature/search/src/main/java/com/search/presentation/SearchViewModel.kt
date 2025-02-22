package com.search.presentation

import androidx.lifecycle.viewModelScope
import com.business.model.Contact
import com.business.usecase.LoadContactsUseCase
import com.common.base.BaseViewModel
import com.common.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val loadContactsUseCase: LoadContactsUseCase,
): BaseViewModel() {
    private val _cachedList: MutableList<Contact> = mutableListOf()

    private val _contactList = MutableStateFlow<DataState<List<Contact>>>(DataState.Idle)
    val contactList = _contactList.asStateFlow()

    fun loadContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            loadContactsUseCase().collectLatest { data ->
                if (data is DataState.Success) {
                    _cachedList.addAll(data.data)
                }
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            val tempContacts = mutableListOf<Contact>()
            for (contact in _cachedList) {
                if (contact.name.lowercase().contains(query.lowercase())) {
                    tempContacts.add(contact)
                }
            }
            _contactList.value = DataState.Success(tempContacts)
        }
    }
}