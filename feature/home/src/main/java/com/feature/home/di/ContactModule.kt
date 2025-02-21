package com.feature.home.di

import com.feature.home.business.repository.ContactsRepository
import com.feature.home.data.repository.ContactsRepositoryImpl
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