package com.business.di

import com.business.repository.ContactsRepository
import com.business.repository.impl.ContactsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface ContactModule {

    @ViewModelScoped
    @Binds
    fun bindContactsRepository(repository: ContactsRepositoryImpl): ContactsRepository

}