package com.mukiva.rssreader.addrss.data.parsing.handlers

import com.mukiva.rssreader.addrss.data.parsing.StackParsingService

abstract class BaseStackParseHandler(
    private val _context: StackParsingService
) : ParseXmlHandler {

    override fun handleStartTag(tag: String) {
        _context.addTag(tag)
    }

    override fun handleEndTag(tag: String) {
        _context.popTag()
    }

    override fun handleText(text: String, peekTag: String) {}

    protected open fun buildResult() {
        _context.popHandler()
    }
}