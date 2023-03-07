package com.mukiva.rssreader.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentFeedItemBinding
import com.mukiva.rssreader.viewmodel.FeedItemViewModel


class FeedItemFragment : Fragment(R.layout.fragment_feed_item) {

    private lateinit var _binding: FragmentFeedItemBinding
    private lateinit var _viewModel: FeedItemViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel = ViewModelProvider(this)[FeedItemViewModel::class.java]
        _binding = FragmentFeedItemBinding.bind(view)

        _viewModel.contentText.observe(viewLifecycleOwner, Observer {
            _binding.contentText.text = it
        })
    }
}