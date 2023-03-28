package com.mukiva.rssreader.addrss.parsing.handlers

import com.mukiva.rssreader.addrss.parsing.StackParsingService
import com.mukiva.rssreader.addrss.parsing.builders.RssBuilder
import com.mukiva.rssreader.addrss.parsing.elements.Rss

class RssParseHandler(
    private val _context: StackParsingService,
    private val _resultSetter: (Rss) -> Unit
) : Rss2StackHandler(_context) {

    private val _rssBuilder = RssBuilder()

    override fun handleStartTag(tag: String) {
        super.handleStartTag(tag)
        when (tag) {
            TAG_RSS -> {
                _rssBuilder.version = _context.getAttr("", ATTR_VERSION) ?: ""
            }
            TAG_CHANNEL -> {
                _context.addHandler(ChannelParseHandler(_context) {
                    _rssBuilder.channel = it
                })
            }
        }
    }

    override fun handleEndTag(tag: String) {
        super.handleEndTag(tag)
        when (tag) {
            TAG_RSS -> buildResult()
        }
    }

    override fun buildResult() {
        super.buildResult()
        _resultSetter(_rssBuilder.build())
    }
}