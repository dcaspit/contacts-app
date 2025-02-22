package com.ui.presentation.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import com.ui.R
import com.ui.databinding.LayoutDialogAlertBinding
import com.ui.extensions.setTextIfAvailableOrHide
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class AlertDialog  @Inject constructor(@ActivityContext context: Context) : Dialog(context, R.style.Theme_Dialog) {
    private val binding: LayoutDialogAlertBinding = LayoutDialogAlertBinding.inflate(
        LayoutInflater.from(context),
        null,
        false
    )

    init {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(binding.root)
    }

    fun setTitle(title: String?) = apply {
        binding.dialogAlertTextviewTitle.text = title
    }

    fun setMessage(message: String?) = apply {
        binding.dialogAlertTextviewMessage.setTextIfAvailableOrHide(message)
    }

    fun setHeaderImage(@DrawableRes resId: Int) = apply {
        binding.dialogAlertImageviewImage.setImageResource(resId)
    }

    fun setPositiveButton(
        text: CharSequence?,
        onClickListener: (dialog: AlertDialog) -> Unit,
    ) = apply {
        with(binding.dialogAlertTextviewPositiveAction) {
            this.text = text
            setOnClickListener {
                onClickListener(this@AlertDialog)
            }
        }
    }
}