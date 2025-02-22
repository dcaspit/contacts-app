package com.feature.home.presentation

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.extensions.collectLatestLifecycleFlow
import com.common.state.DataState
import com.common.util.theme.ThemeUtils
import com.feature.home.R
import com.feature.home.databinding.FragmentHomeBinding
import com.ui.presentation.adapters.ContactListAdapter
import com.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.ui.R as uiR

private const val CONTACTS_PERMISSION_REQUEST = 1

private const val CONTACTS_PERMISSION = "android.permission.READ_CONTACTS"

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(
    inflate = FragmentHomeBinding::inflate
) {
    override val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var themeUtils: ThemeUtils

    @Inject
    lateinit var adapter: ContactListAdapter

    override fun initUi() {
        initToolbar()
        initRecyclerView()
        subscribeContacts()
        requestContactsPermissionOrStartLoadingList()
    }

    private fun initToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment)
        )

        with(binding.toolbar) {
            inflateMenu(uiR.menu.toolbar_main_menu)
            title = "Home"
            setupWithNavController(navController, appBarConfiguration)
            setOnMenuItemClickListener {
                if (it.itemId == uiR.id.item_toolbar_toggle_theme) {
                    toggleTheme()
                    return@setOnMenuItemClickListener true
                }
                false
            }
            setOnClickListener {
                binding.contactList.smoothScrollToPosition(0)
            }
        }
    }

    private fun toggleTheme() {
        themeUtils.toggleTheme(requireContext())
    }

    private fun initRecyclerView() {
        adapter.onItemClicked = ::openContactDetail
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshContacts()
        }
        binding.contactList.addItemDecoration(DividerItemDecoration(binding.root.context, LinearLayoutManager.VERTICAL))
        binding.contactList.adapter = adapter
    }

    private fun openContactDetail(contact: com.business.model.Contact) {
        // TODO: Implement Contact Details Screen
    }

    private fun subscribeContacts() {
        collectLatestLifecycleFlow(viewModel.contactList) { state ->
            when (state) {
                DataState.Idle -> {}
                is DataState.Success -> {
                    adapter.submitList(state.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }

        }
    }

    private fun requestContactsPermissionOrStartLoadingList() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                CONTACTS_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestContactSPermission()
            return
        }
        viewModel.loadContacts()
    }

    private fun requestContactSPermission() {
        requestPermissions(arrayOf(CONTACTS_PERMISSION), CONTACTS_PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONTACTS_PERMISSION_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.loadContacts()
                } else {
                    Toast.makeText(
                        activity, "bla bla",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }
}