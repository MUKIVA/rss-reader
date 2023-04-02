package com.mukiva.rssreader.addrss.data.parsing.handlers

import com.mukiva.rssreader.addrss.data.parsing.elements.Category
import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.data.parsing.elements.Cloud

class ChannelParseHandler(
    private val _context: com.mukiva.rssreader.addrss.data.parsing.StackParsingService,
    private val _resultSetter: (Channel) -> Unit
) : Rss2StackHandler(_context) {

    private val _channelBuilder = com.mukiva.rssreader.addrss.data.parsing.builders.ChannelBuilder()
    private var _category: Category? = null

    override fun handleStartTag(tag: String) {
        super.handleStartTag(tag)
        when (tag) {
            TAG_CATEGORY -> _category = Category(
                text = "",
                domain = _context.getAttr("", ATTR_DOMAIN)
            )
            TAG_IMAGE -> _context.addHandler(ImageParseHandler(_context) {
                _channelBuilder.image = it
            })
            TAG_CLOUD -> _channelBuilder.cloud = Cloud(
                domain = _context.getAttr("", ATTR_DOMAIN) ?: "",
                path = _context.getAttr("", ATTR_PATH) ?: "",
                port = _context.getAttr("", ATTR_PORT) ?: "",
                protocol = _context.getAttr("", ATTR_PROTOCOL) ?: "",
                registerProcedure = _context.getAttr("", ATTR_REGISTER_PROCEDURE) ?: ""
            )
            TAG_TEXT_INPUT -> _context.addHandler(TextInputParseHandler(_context) {
                _channelBuilder.textInput = it
            })
            TAG_ITEM -> _context.addHandler(ItemParseHandler(_context) {
                _channelBuilder.addItem(it)
            })
        }
    }

    override fun handleText(text: String, peekTag: String) {
        val trimmedText = text.trim()
        when (peekTag) {
            TAG_RATING -> _channelBuilder.rating = trimmedText
            TAG_CATEGORY -> _category = _category!!.copy(text = trimmedText)
            TAG_TITLE -> _channelBuilder.title = trimmedText
            TAG_LINK -> _channelBuilder.link = trimmedText
            TAG_DESCRIPTION -> _channelBuilder.description = trimmedText
            TAG_LANGUAGE -> _channelBuilder.language = trimmedText
            TAG_COPYRIGHT -> _channelBuilder.copyright = trimmedText
            TAG_MANAGING_EDITOR -> _channelBuilder.managingEditor = trimmedText
            TAG_WEB_MASTER -> _channelBuilder.webMaster = trimmedText
            TAG_PUB_DATE -> _channelBuilder.pubDate = dateFormatter.parse(trimmedText)
            TAG_LAST_BUILD_DATE -> _channelBuilder.lastBuildDate = dateFormatter.parse(trimmedText)
            TAG_GENERATOR -> _channelBuilder.generator = trimmedText
            TAG_DOCS -> _channelBuilder.docs = trimmedText
            TAG_TTL -> _channelBuilder.ttl = trimmedText
            TAG_SKIP_HOURS -> _channelBuilder.skipHours = trimmedText.toInt()
            TAG_SKIP_DAYS -> _channelBuilder.skipDays = trimmedText.toInt()
        }
    }

    override fun handleEndTag(tag: String) {
        super.handleEndTag(tag)
        when (tag) {
            TAG_CATEGORY -> _channelBuilder.addCategory(_category!!)
            TAG_CHANNEL -> buildResult()
        }
    }

    override fun buildResult() {
        super.buildResult()
        _resultSetter(_channelBuilder.build())
    }
}