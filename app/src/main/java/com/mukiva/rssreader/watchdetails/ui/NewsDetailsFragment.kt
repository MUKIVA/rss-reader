//TODO(Migrate to compose)
//package com.mukiva.rssreader.watchdetails.ui
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import androidx.core.view.MenuHost
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.Lifecycle
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.DataSource
//import com.bumptech.glide.load.engine.GlideException
//import com.bumptech.glide.request.RequestListener
//import com.bumptech.glide.request.target.Target
//import com.mukiva.rssreader.R
//import com.mukiva.rssreader.databinding.FragmentWatchDetailsBinding
//import com.mukiva.rssreader.watchdetails.di.factory
//import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsState
//import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsStateType.*
//import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsStateType
//import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsViewModel
//import kotlinx.coroutines.FlowPreview
//import java.util.*
//
//@FlowPreview
//class NewsDetailsFragment : Fragment(R.layout.fragment_watch_details) {
//
//    private lateinit var _binding: FragmentWatchDetailsBinding
//    private val _viewModel: WatchDetailsViewModel by viewModels { factory() }
//    private lateinit var _shareLink: String
//
//    private val _imageLoadListener = object : RequestListener<Drawable> {
//        override fun onLoadFailed(
//            e: GlideException?,
//            model: Any?,
//            target: Target<Drawable>?,
//            isFirstResource: Boolean
//        ): Boolean {
//            _binding.fragmentFeedItemImage.isVisible = false
//            setPanelsState(NORMAL)
//            initMenu()
//            return false
//        }
//
//        override fun onResourceReady(
//            resource: Drawable?,
//            model: Any?,
//            target: Target<Drawable>?,
//            dataSource: DataSource?,
//            isFirstResource: Boolean
//        ): Boolean {
//            _binding.fragmentFeedItemImage.isVisible = true
//            setPanelsState(NORMAL)
//            initMenu()
//            return false
//        }
//    }
//
//
//    companion object {
//        const val ARG_ITEM_ID = "ARG_ITEM_ID"
//    }
//
//    private val _menuProvider = WatchDetailsMenuProvider(
//        object : WatchDetailsMenuActions {
//
//            override fun share() {
//                val sendIntent: Intent = Intent(Intent.ACTION_SEND).apply {
//                    type = "text/plain"
//                    putExtra(Intent.EXTRA_TEXT, _shareLink)
//                }
//
//                val shareIntent = Intent.createChooser(sendIntent, null)
//                startActivity(shareIntent)
//            }
//        }
//    )
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initFields(view)
//        observeViewModel()
//        initContent()
//    }
//
//    @SuppressLint("CheckResult")
//    private fun initContent() {
//        val bundle = requireArguments()
//        val itemId = bundle.getLong(ARG_ITEM_ID)
//
//        _viewModel.setNews(itemId)
//    }
//
//    private fun setButtonLink(link: String) {
//        _binding.fragmentFeedItemOpenOriginalButton.setOnClickListener {
//            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
//            startActivity(browserIntent)
//        }
//    }
//
//    private fun initFields(view: View) {
//        _binding = FragmentWatchDetailsBinding.bind(view)
//    }
//
//    private fun observeViewModel() {
//        _viewModel.state.observe(viewLifecycleOwner, ::render)
//    }
//
//
//
//    private fun render(state: WatchDetailsState) {
//        when (state.stateType) {
//            NORMAL -> renderNormalState(state)
//            LOADING -> setPanelsState(state.stateType)
//        }
//    }
//
//    private fun renderNormalState(state: WatchDetailsState) {
//        _binding.feedItemDescriptionText.text = state.title
//        _binding.fragmentFeedItemDate.text = state.dateString
//        _binding.fragmentFeedItemContent.text = state.description
//
//        state.originalLink.apply {
//            setButtonLink(this)
//            _shareLink = this
//        }
//
//        Glide.with(this)
//            .load(state.imageLink)
//            .dontAnimate()
//            .override(1920, 1080)
//            .listener(_imageLoadListener)
//            .into(_binding.fragmentFeedItemImage)
//    }
//
//    private fun setPanelsState(stateType: WatchDetailsStateType) {
//        _binding.loader.root.isVisible = stateType == LOADING
//        _binding.detailsContent.isVisible = stateType == NORMAL
//    }
//
//    private fun initMenu() {
//        (requireActivity() as MenuHost)
//            .addMenuProvider(_menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
//    }
//}