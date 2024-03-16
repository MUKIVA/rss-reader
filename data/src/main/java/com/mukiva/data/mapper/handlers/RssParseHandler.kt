package com.mukiva.data.mapper.handlers

import com.mukiva.data.entity.Rss
import com.mukiva.data.mapper.StackParser
import com.mukiva.data.mapper.builders.RssBuilder

class RssParseHandler(
    private val _context: StackParser,
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