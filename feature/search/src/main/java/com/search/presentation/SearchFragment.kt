package com.search.presentation

import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.business.model.Contact
import com.common.extensions.collectLatestLifecycleFlow
import com.common.state.DataState
import com.common.util.theme.ThemeUtils
import com.search.databinding.FragmentSearchBinding
import com.ui.base.BaseFragment
import com.ui.presentation.adapters.ContactListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: BaseFragment<FragmentSearchBinding>(
    inflate = FragmentSearchBinding::inflate
) {
    override val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var themeUtils: ThemeUtils

    @Inject
    lateinit var adapter: ContactListAdapter

    override fun initUi() {
        initToolbar()
        initRecyclerView()
        initSearchView()
        subscribeContacts()
    }

    private fun initSearchView() {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                return false
            }
        })
    }

    private fun subscribeContacts() {
        collectLatestLifecycleFlow(viewModel.contactList) { state ->
            when (state) {
                DataState.Idle -> {}
                is DataState.Success -> {
                    adapter.submitList(state.data)
                }
            }

        }
    }

    private fun initRecyclerView() {
        adapter.onItemClicked = ::openContactDetail
        binding.contactList.addItemDecoration(DividerItemDecoration(binding.root.context, LinearLayoutManager.VERTICAL))
        binding.contactList.adapter = adapter
    }

    private fun openContactDetail(contact: Contact) {
        // TODO: Implement Contact Details Screen
    }

    private fun initToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(com.search.R.id.searchFragment)
        )

        with(binding.toolbar) {
            inflateMenu(com.ui.R.menu.toolbar_main_menu)
            title = "Search"
            setupWithNavController(navController, appBarConfiguration)
            setOnMenuItemClickListener {
                if (it.itemId == com.ui.R.id.item_toolbar_toggle_theme) {
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
}