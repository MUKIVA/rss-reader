package com.mukiva.rssreader.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.ViewSearchRssBinding

class SearchRssView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var _binding: ViewSearchRssBinding

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

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

            when (typedArray.getBoolean(R.styleable.SearchRssView_field_isProgress, false)) {
                true -> searchRssProgressBar.visibility = VISIBLE
                false -> searchRssProgressBar.visibility = GONE
            }
        }

        typedArray.recycle()
    }
}