package com.mukiva.rssreader.watchdetails.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentWatchDetailsBinding
import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsState
import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsStateType
import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsViewModel

class WatchDetailsMenuProvider : MenuProvider {
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.feed_item_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }
}

class WatchDetailsFragment : Fragment(R.layout.fragment_watch_details) {

    private lateinit var _binding: FragmentWatchDetailsBinding
    private lateinit var _viewModel: WatchDetailsViewModel
    private val _menuProvider = WatchDetailsMenuProvider()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields(view)
        observeViewModel()

//        initMenu()
    }

    private fun initFields(view: View) {
        _viewModel = ViewModelProvider(this)[WatchDetailsViewModel::class.java]
        _binding = FragmentWatchDetailsBinding.bind(view)
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner) { state -> render(state) }
    }

    private fun render(state: WatchDetailsState) {
        when (state.stateType) {
            WatchDetailsStateType.NORMAL -> renderNormalState()
            WatchDetailsStateType.PARSE_ERROR -> renderParseErrorState()
        }
    }

    private fun renderNormalState() {

    }

    private  fun renderParseErrorState() {

    }

    private fun initMenu() {
        (requireActivity() as MenuHost)
            .addMenuProvider(_menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}