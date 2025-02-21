package com.common.di

import com.common.util.theme.ThemeUtils
import com.common.util.theme.impl.ThemeUtilsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ThemeUtilsModule {

    @Singleton
    @Binds
    fun bindThemeUtils(impl: ThemeUtilsImpl): ThemeUtils
}