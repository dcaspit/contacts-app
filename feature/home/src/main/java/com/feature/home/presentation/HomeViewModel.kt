package com.feature.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.common.base.BaseViewModel
import com.common.state.DataState
import com.feature.home.business.model.Contact
import com.feature.home.business.usecase.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
): BaseViewModel(){

    private val _contactList = MutableStateFlow<DataState<List<Contact>>>(DataState.Idle)
    val contactList = _contactList.asStateFlow()

    fun getContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            getContactsUseCase().collectLatest {
                _contactList.value = it
            }
        }
    }
}