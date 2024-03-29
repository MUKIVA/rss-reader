package com.mukiva.data.mapper.handlers

import com.mukiva.data.entity.Image
import com.mukiva.data.mapper.StackParser
import com.mukiva.data.mapper.builders.ImageBuilder

class ImageParseHandler(
    context : StackParser,
    private val _resultSetter : (Image) -> Unit
) : Rss2StackHandler(context) {

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