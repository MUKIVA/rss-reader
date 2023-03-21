package com.mukiva.rssreader.watchfeeds.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.ViewFeedInfoBinding

class FeedInfoBottomSheetDialog
   @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val _binding: ViewFeedInfoBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_feed_info, this, true)
        _binding = ViewFeedInfoBinding.bind(this)
    }

    fun setTitle(text: String) {
        _binding.feedInfoTitle.text = text
    }

    fun setDescription(text: String) {
        _binding.feedInfoDescription.text = text
    }

    fun setOpenOriginalAction(action: OnClickListener) {
        _binding.feedInfoOpenOriginalBtn.setOnClickListener(action)
    }
}