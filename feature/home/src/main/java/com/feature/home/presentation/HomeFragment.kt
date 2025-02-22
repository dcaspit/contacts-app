package com.feature.home.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import com.feature.home.business.model.Contact
import com.feature.home.databinding.FragmentHomeBinding
import com.feature.home.presentation.adapters.ContactListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.ui.R as uiR

private const val CONTACTS_PERMISSION_REQUEST = 1

private const val CONTACTS_PERMISSION = "android.permission.READ_CONTACTS"

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var themeUtils: ThemeUtils

    @Inject
    lateinit var adapter: ContactListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initRecyclerView()

        viewModel.contactList.observe(viewLifecycleOwner) { state ->
            when(state) {
                is DataState.Error<*> -> {

                }
                DataState.Idle -> {}
                DataState.Loading -> {}
                is DataState.Success -> {
                    adapter.submitList(state.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }

        }

        requestContactsPermissionOrStartLoadingList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openContactDetail(contact: Contact) {
        // TODO: Implement Contact Details Screen
    }

    private fun initRecyclerView() {
        adapter.onItemClicked = ::openContactDetail
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getContacts()
        }
        binding.contactList.addItemDecoration(DividerItemDecoration(binding.root.context, LinearLayoutManager.VERTICAL))
        binding.contactList.adapter = adapter
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


    private fun requestContactsPermissionOrStartLoadingList() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                CONTACTS_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestContactSPermission()
            return
        }

        viewModel.getContacts()
    }

    /**
     * Show the user a dialog asking for permission to show contacts.
     */
    private fun requestContactSPermission() {
        requestPermissions(arrayOf(CONTACTS_PERMISSION), CONTACTS_PERMISSION_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONTACTS_PERMISSION_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getContacts()
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