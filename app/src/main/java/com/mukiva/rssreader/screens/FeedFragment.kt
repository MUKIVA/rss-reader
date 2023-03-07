package com.mukiva.rssreader.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.button.MaterialButton
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentFeedBinding
import com.mukiva.rssreader.viewmodel.FeedViewModel


class FeedFragment : Fragment(R.layout.fragment_feed) {

    private lateinit var _binding: FragmentFeedBinding
    private lateinit var _viewModel: FeedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel = ViewModelProvider(this)[FeedViewModel::class.java]
        _binding = FragmentFeedBinding.bind(view)
        _binding.goAddRss.setOnClickListener { goAddRss() }
        _binding.goFeedItem.setOnClickListener { goFeedItem() }
        _binding.updateTextButton.setOnClickListener { updateContentText() }

        _viewModel.contentText.observe(viewLifecycleOwner, Observer {
            _binding.content.text = it
        })
    }

    private fun updateContentText() {
        _viewModel.updateContentText("Updated text!")
    }

    private fun goAddRss() {
        findNavController().navigate(
            R.id.action_feedFragment_to_addRssFragment, null,
            navOptions {
                anim {
                    enter = R.anim.enter
                    exit = R.anim.exit
                    popEnter = R.anim.pop_enter
                    popExit = R.anim.pop_exit
                }
            })
    }

    private fun goFeedItem() {
        findNavController().navigate(
            R.id.action_feedFragment_to_feedItemFragment, null,
            navOptions {
                anim {
                    enter = R.anim.enter
                    exit = R.anim.exit
                    popEnter = R.anim.pop_enter
                    popExit = R.anim.pop_exit
                }
            })
    }
}