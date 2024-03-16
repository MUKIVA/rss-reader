package com.mukiva.data.mapper.handlers

import com.mukiva.data.mapper.StackParser

abstract class BaseStackParseHandler(
    private val _context: StackParser
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