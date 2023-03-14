package com.mukiva.rssreader.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.ViewRssItemBinding

class RssItemView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var _binding: ViewRssItemBinding

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_rss_item, this, true)

        _binding = ViewRssItemBinding.bind(this)
        initializeAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initializeAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RssItemView, defStyleAttr, defStyleRes)

        with(_binding) {
            val descriptionText = typedArray.getString(R.styleable.RssItemView_descriptionText)
            rssItemDescriptionText.text = descriptionText ?: resources.getString(R.string.mock_description)

            val titleText = typedArray.getString(R.styleable.RssItemView_titleText)
            rssItemTitleText.text = titleText ?: resources.getString(R.string.mock_title)
        }

        typedArray.recycle()
    }
}