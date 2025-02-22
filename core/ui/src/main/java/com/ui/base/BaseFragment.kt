package com.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.common.base.BaseViewModel
import com.ui.presentation.dialogs.AlertDialog
import javax.inject.Inject

abstract class BaseFragment <VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    protected abstract val viewModel: BaseViewModel

    @Inject
    lateinit var alertDialog: AlertDialog

    private var _binding: VB? = null
    val binding: VB
        @Suppress("UnsafeCallOnNullableType")
        get() = _binding!!

    protected abstract fun initUi()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}