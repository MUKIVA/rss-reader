package com.mukiva.rssreader.addrss.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.presentation.AddRssState
import com.mukiva.rssreader.addrss.presentation.AddRssStateType
import com.mukiva.rssreader.databinding.FragmentAddRssBinding
import com.mukiva.rssreader.addrss.presentation.AddRssViewModel

class AddRssFragment : Fragment(R.layout.fragment_add_rss) {
    private lateinit var _binding: FragmentAddRssBinding
    private lateinit var _viewModel: AddRssViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields(view)
        observeViewModel()
        setListeners()
    }

    private fun setListeners() {

    }

    private fun initFields(view: View) {
        _viewModel = ViewModelProvider(this)[AddRssViewModel::class.java]
        _binding = FragmentAddRssBinding.bind(view)
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner) { state -> render(state) }
    }

    private fun render(state: AddRssState) {

        when(state.stateType) {
            AddRssStateType.NORMAL -> renderNormalState()
            AddRssStateType.SEARCH -> renderSearchState()
            AddRssStateType.SEARCH_FAIL -> renderSearchFailState()
            AddRssStateType.SEARCH_SUCCESS -> renderSearchSuccessState()
        }

    }

    private fun renderNormalState() {

    }

    private fun renderSearchState() {

    }

    private fun renderSearchFailState() {

    }

    private fun renderSearchSuccessState() {

    }

}