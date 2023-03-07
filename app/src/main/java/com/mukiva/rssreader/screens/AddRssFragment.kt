package com.mukiva.rssreader.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentAddRssBinding
import com.mukiva.rssreader.viewmodel.AddRssViewModel

class AddRssFragment : Fragment(R.layout.fragment_add_rss) {

    private lateinit var _binding: FragmentAddRssBinding
    private lateinit var _viewModel: AddRssViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel = ViewModelProvider(this)[AddRssViewModel::class.java]
        _binding = FragmentAddRssBinding.bind(view)
        _viewModel.contentText.observe(viewLifecycleOwner, Observer {
            _binding.contentText.text = it
        })
    }
}