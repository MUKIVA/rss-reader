package com.mukiva.rssreader.addrss.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.ViewSearchRssBinding
import com.mukiva.rssreader.watchfeeds.domain.Feed

class SearchRssView
@JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var _binding: ViewSearchRssBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_search_rss, this, true)
        _binding = ViewSearchRssBinding.bind(this)
        initializeAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initializeAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchRssView, defStyleAttr, defStyleRes)

        with(_binding) {
            val fieldHint = typedArray.getString(R.styleable.SearchRssView_field_hint) ?: resources.getString(R.string.search_rss_hint)
            searchRssField.hint = fieldHint

            val fieldIsProgress = typedArray.getBoolean(R.styleable.SearchRssView_field_isProgress, false)
            searchRssProgressBar.isVisible = fieldIsProgress
        }

        typedArray.recycle()
    }

    fun setRssItem(item: Feed?) {
        with (_binding) {
            rssItemTitleText.text = item?.title ?: "Undefined"
            rssItemDescriptionText.text = item?.description ?: "Undefined"
        }
    }

    var errorMessageText: String
        get() = _binding.errorText.text.toString()
        set(value) {
            _binding.errorText.text = value
        }

    var errorMsgIsVisible: Boolean
        get() = _binding.errorText.isVisible
        set(value) {
            _binding.errorText.isVisible = value
        }

    var previewAreaIsVisible: Boolean
        get() = _binding.previewArea.isVisible
        set(value) {
            _binding.previewArea.isVisible = value
        }

    var inProgress: Boolean
        get() = _binding.searchRssProgressBar.isVisible
        set(value) {
            _binding.searchRssProgressBar.isVisible = value
        }

    fun setFieldListener(action: (CharSequence?, Int, Int, Int) -> Unit) {
        _binding.searchRssField.doOnTextChanged(action)
    }

    fun setBtnListener(onClickListener: OnClickListener) {
        _binding.addToFeedBtn.setOnClickListener(onClickListener)
    }
}