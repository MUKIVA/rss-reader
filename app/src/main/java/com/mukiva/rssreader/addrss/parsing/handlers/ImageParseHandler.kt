package com.mukiva.rssreader.addrss.parsing.handlers

import com.mukiva.rssreader.addrss.parsing.StackParsingService
import com.mukiva.rssreader.addrss.parsing.builders.ImageBuilder
import com.mukiva.rssreader.addrss.parsing.elements.Image

class ImageParseHandler(
    _context : StackParsingService,
    private val _resultSetter : (Image) -> Unit
) : Rss2StackHandler(_context) {

    private val _imageBuilder = ImageBuilder()

    override fun handleText(text: String, peekTag: String) {
        val trimmedText = text.trim()
        when (peekTag) {
            TAG_URL -> _imageBuilder.url = trimmedText
            TAG_TITLE -> _imageBuilder.title = trimmedText
            TAG_LINK -> _imageBuilder.link = trimmedText
            TAG_WIDTH -> _imageBuilder.width = trimmedText.toInt()
            TAG_HEIGHT -> _imageBuilder.height = trimmedText.toInt()
            TAG_DESCRIPTION -> _imageBuilder.description = trimmedText
        }
    }

    override fun handleEndTag(tag: String) {
        super.handleEndTag(tag)
        when (tag) {
            TAG_IMAGE -> buildResult()
        }
    }

    override fun buildResult() {
        super.buildResult()
        _resultSetter(_imageBuilder.build())
    }
}