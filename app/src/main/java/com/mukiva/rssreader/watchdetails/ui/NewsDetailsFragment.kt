package com.mukiva.rssreader.watchdetails.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentWatchDetailsBinding
import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsState
import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsStateType
import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsViewModel
import java.text.SimpleDateFormat
import java.util.*

class NewsDetailsFragment : Fragment(R.layout.fragment_watch_details) {

    private lateinit var _binding: FragmentWatchDetailsBinding
    private lateinit var _viewModel: WatchDetailsViewModel
    private lateinit var _shareLink: String
    companion object {
        const val ARG_TITLE = "title"
        const val ARG_DESCRIPTION = "description"
        const val ARG_DATE = "date"
        const val ARG_IMAGE_LINK = "imageLink"
        const val ARG_ORIGINAL_LINK = "originalLink"
    }

    private val _menuProvider = WatchDetailsMenuProvider(
        object : WatchDetailsMenuActions {

            override fun share() {
                val sendIntent: Intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, _shareLink)
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields(view)
        observeViewModel()

        initMenu()
        initContent()
    }

    @SuppressLint("CheckResult")
    private fun initContent() {
        val bundle = requireArguments()
        val title = bundle.getString(ARG_TITLE)
        val desc = bundle.getString(ARG_DESCRIPTION)
        val imageLink = bundle.getString(ARG_IMAGE_LINK)
        val date = bundle.getLong(ARG_DATE)
        val originalLink = bundle.getString(ARG_ORIGINAL_LINK)

        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        _shareLink = originalLink!!
        _binding.fragmentFeedItemContent.text = desc
        _binding.feedItemDescriptionText.text = title
        _binding.fragmentFeedItemDate.text = formatter.format(Date(date))
        _binding.fragmentFeedItemOpenOriginalButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(originalLink))
            startActivity(browserIntent)
        }
        if (imageLink == null) {
            _binding.fragmentFeedItemImage.isVisible = false
            return
        }

        with(_binding.fragmentFeedItemImage) {
            Glide.with(this@NewsDetailsFragment).load(imageLink)
                .into(this)
                .onLoadFailed(ContextCompat.getDrawable(context, R.drawable.ic_no_image))
            isVisible = true
        }

    }

    private fun initFields(view: View) {
        _viewModel = ViewModelProvider(this)[WatchDetailsViewModel::class.java]
        _binding = FragmentWatchDetailsBinding.bind(view)
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: WatchDetailsState) {
        when (state.stateType) {
            WatchDetailsStateType.NORMAL -> renderNormalState()
        }
    }

    private fun renderNormalState() {

    }

    private fun initMenu() {
        (requireActivity() as MenuHost)
            .addMenuProvider(_menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}