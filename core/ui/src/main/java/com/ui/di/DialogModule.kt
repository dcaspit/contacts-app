package com.ui.di

import android.app.Dialog
import com.ui.presentation.dialogs.AlertDialog
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
interface DialogModule {
    @ActivityScoped
    @Binds
    fun bindAlertDialog(impl: AlertDialog): Dialog
}