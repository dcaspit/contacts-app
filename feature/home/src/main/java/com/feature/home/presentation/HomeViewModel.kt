package com.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.common.base.BaseViewModel
import com.common.state.DataState
import com.business.model.Contact
import com.business.usecase.LoadContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadContactsUseCase: LoadContactsUseCase,
): BaseViewModel(){

    private val _contactList = MutableStateFlow<DataState<List<Contact>>>(DataState.Idle)
    val contactList = _contactList.asStateFlow()

    fun loadContacts() {
        if(_contactList.value is DataState.Idle) {
            viewModelScope.launch(Dispatchers.IO) {
                loadContactsUseCase().collectLatest { data ->
                    _contactList.value = data
                }
            }
        }
    }

    fun refreshContacts() {
        _contactList.value = DataState.Idle
        loadContacts()
    }
}