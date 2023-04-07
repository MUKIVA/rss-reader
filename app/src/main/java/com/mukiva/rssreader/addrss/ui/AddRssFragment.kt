@file:OptIn(FlowPreview::class)

package com.mukiva.rssreader.addrss.ui

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.di.factory
import com.mukiva.rssreader.addrss.presentation.AddRssEvent
import com.mukiva.rssreader.addrss.presentation.AddRssState
import com.mukiva.rssreader.addrss.presentation.AddRssStateType
import com.mukiva.rssreader.databinding.FragmentAddRssBinding
import com.mukiva.rssreader.addrss.presentation.AddRssViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddRssFragment : Fragment(R.layout.fragment_add_rss) {
    private lateinit var _binding: FragmentAddRssBinding
    private lateinit var _inputMethodManager: InputMethodManager
    private val _viewModel: AddRssViewModel by viewModels { factory() }

    private var _onAddRssEnd: () -> Unit = {
        _inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields(view)
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        initActions()
    }

    fun setAddRssEndListener(listener: () -> Unit) {
        _onAddRssEnd = listener
    }

    private fun initActions() {

        _binding.searchField.setFieldListener { text, _, _, _ ->
                _viewModel.triggerSearch(text.toString())
        }

        _binding.searchField.setBtnListener {
            _viewModel.addRss()

        }
    }

    @SuppressLint("ServiceCast")
    private fun initFields(view: View) {
        _binding = FragmentAddRssBinding.bind(view)
        _inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner, ::render)

        _viewModel.event
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { handleEvents(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleEvents(event: AddRssEvent) {
        when (event) {
            AddRssEvent.AddRssEnd -> handleEndAddEvent()
        }
    }

    private fun handleEndAddEvent() {
        _onAddRssEnd()
    }

    private fun render(state: AddRssState) {
        _binding.searchField.errorMessageText = state.errorMessage?.let { getString(it) }.toString()
        state.rssItem?.let { _binding.searchField.setRssItem(it) }
        when(state.stateType) {
            AddRssStateType.NORMAL -> renderNormalState()
            AddRssStateType.SEARCH -> renderSearchState()
            AddRssStateType.SEARCH_FAIL -> renderSearchFailState()
            AddRssStateType.SEARCH_SUCCESS -> renderSearchSuccessState()
            AddRssStateType.LOCK -> renderLockState()
        }
    }

    private fun renderLockState() {
        _binding.searchField.setButtonLock(true)
        _binding.searchField.setFieldLock(true)
    }

    private fun renderNormalState() {
        _binding.searchField.clearField()
        _binding.searchField.inProgress = false
        _binding.searchField.errorMsgIsVisible = false
        _binding.searchField.previewAreaIsVisible = false
    }

    private fun renderSearchState() {
        _binding.searchField.inProgress = true
        _binding.searchField.errorMsgIsVisible = false
        _binding.searchField.previewAreaIsVisible = false
    }

    private fun renderSearchFailState() {
        _binding.searchField.inProgress = false
        _binding.searchField.errorMsgIsVisible = true
        _binding.searchField.previewAreaIsVisible = false
    }

    private fun renderSearchSuccessState() {
        _binding.searchField.setButtonLock(false)
        _binding.searchField.setFieldLock(false)
        _binding.searchField.inProgress = false
        _binding.searchField.errorMsgIsVisible = false
        _binding.searchField.previewAreaIsVisible = true
    }
}