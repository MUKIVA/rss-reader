package com.mukiva.rssreader.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.ViewSearchRssBinding

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

            when (typedArray.getBoolean(R.styleable.SearchRssView_field_isProgress, false)) {
                true -> searchRssProgressBar.visibility = VISIBLE
                false -> searchRssProgressBar.visibility = GONE
            }
        }

        typedArray.recycle()
    }
}