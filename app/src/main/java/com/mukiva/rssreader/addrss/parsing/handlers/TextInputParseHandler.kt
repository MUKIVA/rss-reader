package com.mukiva.rssreader.addrss.parsing.handlers

import com.mukiva.rssreader.addrss.parsing.StackParsingService
import com.mukiva.rssreader.addrss.parsing.builders.TextInputBuilder
import com.mukiva.rssreader.addrss.parsing.elements.TextInput

class TextInputParseHandler(
    _context: StackParsingService,
    private val _resultSetter: (TextInput) -> Unit
) : BaseStackParseHandler(_context) {

    companion object {
        const val TAG_TEXT_INPUT = "textInput"
        const val TAG_TITLE = "title"
        const val TAG_DESCRIPTION = "description"
        const val TAG_NAME = "name"
        const val TAG_LINK = "link"
    }

    private val _textInputBuilder = TextInputBuilder()

    override fun handleText(text: String, peekTag: String) {
        val trimmedText = text.trim()
        when (peekTag) {
            TAG_TITLE -> _textInputBuilder.title = trimmedText
            TAG_DESCRIPTION -> _textInputBuilder.description = trimmedText
            TAG_NAME -> _textInputBuilder.name = trimmedText
            TAG_LINK -> _textInputBuilder.link = trimmedText
        }
    }

    override fun handleEndTag(tag: String) {
        super.handleEndTag(tag)
        when (tag) {
            TAG_TEXT_INPUT -> buildResult()
        }
    }

    override fun buildResult() {
        super.buildResult()
        _resultSetter(_textInputBuilder.build())
    }
}