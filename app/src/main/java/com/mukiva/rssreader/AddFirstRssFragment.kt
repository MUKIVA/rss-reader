package com.mukiva.rssreader

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.mukiva.rssreader.databinding.FragmentAddFirstRssBinding

class AddFirstRssFragment : Fragment(R.layout.fragment_add_first_rss) {

    private lateinit var binding: FragmentAddFirstRssBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddFirstRssBinding.bind(view)

        binding.goFeedButton.setOnClickListener {
            goFeed()
        }
    }

    private fun goFeed() {
        findNavController().navigate(R.id.action_addFirstRssFragment_to_feedFragment, null,
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

