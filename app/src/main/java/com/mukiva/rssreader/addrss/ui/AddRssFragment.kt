@file:OptIn(FlowPreview::class)

package com.mukiva.rssreader.addrss.ui

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.di.factory
import com.mukiva.rssreader.addrss.presentation.AddRssState
import com.mukiva.rssreader.addrss.presentation.AddRssStateType
import com.mukiva.rssreader.databinding.FragmentAddRssBinding
import com.mukiva.rssreader.addrss.presentation.AddRssViewModel
import kotlinx.coroutines.FlowPreview

class AddRssFragment : Fragment(R.layout.fragment_add_rss) {
    private lateinit var _binding: FragmentAddRssBinding
    private val _viewModel: AddRssViewModel by viewModels { factory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields(view)
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        initActions()
    }

    fun getViewModel(): AddRssViewModel = _viewModel

    fun setBtnListener(onClickListener: OnClickListener) {
        _binding.searchField.setBtnListener(onClickListener)
    }

    private fun initActions() {

        _binding.searchField.setFieldListener { text, _, _, _ ->
            _viewModel.triggerSearch(text.toString())
        }

        _binding.searchField.setBtnListener {
            _viewModel.addRss()
            findNavController().navigateUp()
        }
    }

    private fun initFields(view: View) {
        _binding = FragmentAddRssBinding.bind(view)
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: AddRssState) {
        _binding.searchField.errorMessageText = state.errorMessage?.let { getString(it) }.toString()
        state.rssItem?.let { _binding.searchField.setRssItem(it) }
        when(state.stateType) {
            AddRssStateType.NORMAL -> renderNormalState()
            AddRssStateType.SEARCH -> renderSearchState()
            AddRssStateType.SEARCH_FAIL -> renderSearchFailState()
            AddRssStateType.SEARCH_SUCCESS -> renderSearchSuccessState()
        }
    }

    private fun renderNormalState() {
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
        _binding.searchField.inProgress = false
        _binding.searchField.errorMsgIsVisible = false
        _binding.searchField.previewAreaIsVisible = true
    }
}