package com.mukiva.rssreader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.mukiva.rssreader.databinding.FragmentFeedBinding


class FeedFragment : Fragment(R.layout.fragment_feed) {

    private lateinit var binding: FragmentFeedBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFeedBinding.bind(view)

        binding.goAddRss.setOnClickListener {
            goAddRss()
        }

        binding.goFeedItem.setOnClickListener {
            goFeedItem()
        }
    }

    private fun goAddRss() {
        findNavController().navigate(R.id.action_feedFragment_to_addRssFragment, null,
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
        findNavController().navigate(R.id.action_feedFragment_to_feedItemFragment, null,
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