package com.ui.presentation.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.common.base.BaseViewHolder
import com.business.model.Contact
import com.ui.R
import com.ui.databinding.ListItemContactBinding

class ContactsViewHolder(
    private val parent: ViewGroup,
    private val onItemClicked: ((contact: Contact) -> Unit)?,
): BaseViewHolder<Contact, ListItemContactBinding>(
    binding = ListItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
) {
    override fun bind(item: Contact) {
        with(binding) {
            contactImage.setOnClickListener {
                onItemClicked?.invoke(item)
            }
            clickableOverlay.setOnClickListener {
                onItemClicked?.invoke(item)
            }
            if (item.imageUri != null) {
                Glide.with(parent.context)
                    .load(item.imageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(contactImage)
            } else {
                contactImage.setImageResource(R.drawable.ic_face_24)
            }
            if (item.imageUri != null) {
                contactImage.setImageURI(Uri.parse(item.imageUri))
            } else {
                contactImage.setImageURI(null)
            }
            contactName.text = item.name
            contactNumber.text = item.number
        }
    }
}