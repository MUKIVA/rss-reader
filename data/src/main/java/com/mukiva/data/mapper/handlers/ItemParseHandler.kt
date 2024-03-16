package com.mukiva.data.mapper.handlers

import com.mukiva.data.entity.Category
import com.mukiva.data.entity.Enclosure
import com.mukiva.data.entity.Guid
import com.mukiva.data.entity.Item
import com.mukiva.data.entity.Source
import com.mukiva.data.mapper.StackParser
import com.mukiva.data.mapper.builders.ItemBuilder

class ItemParseHandler(
    private val _context: StackParser,
    private val _resultSetter: (Item) -> Unit
) : Rss2StackHandler(_context) {

    private val _itemBuilder = ItemBuilder()
    private var _category: Category? = null

    override fun handleStartTag(tag: String) {
        super.handleStartTag(tag)
        when (tag) {
            TAG_ENCLOSURE -> _itemBuilder.enclosure = Enclosure(
                url = _context.getAttr("", ATTR_URL) ?: "",
                type = _context.getAttr("", ATTR_TYPE) ?: "",
                length = _context.getAttr("", ATTR_LENGTH)?.toLong() ?: 0
            )
            TAG_SOURCE -> _itemBuilder.source = Source(
                url = _context.getAttr("", ATTR_URL) ?: "",
                text = ""
            )
            TAG_CATEGORY -> _category = Category(
                text = "",
                domain = _context.getAttr("", ATTR_DOMAIN)
            )
            TAG_GUID -> _itemBuilder.guid = Guid(
                text = "",
                isPermaLink = _context.getAttr("", ATTR_IS_PERMA_LINK).toBoolean()
            )
        }
    }

    override fun handleText(text: String, peekTag: String) {
        val trimmedText = text.trim()
        if (trimmedText.isBlank()) return
        when (peekTag) {
            TAG_CATEGORY -> _category = _category!!.copy(text = trimmedText)
            TAG_SOURCE -> _itemBuilder.source = _itemBuilder.source!!.copy(text = trimmedText)
            TAG_TITLE -> _itemBuilder.title = trimmedText
            TAG_DESCRIPTION -> _itemBuilder.description += text
            TAG_LINK -> _itemBuilder.link = trimmedText
            TAG_COMMENTS -> _itemBuilder.comments = trimmedText
            TAG_AUTHOR -> _itemBuilder.author = trimmedText
            TAG_GUID -> _itemBuilder.guid = _itemBuilder.guid!!.copy(text = trimmedText)
            TAG_PUB_DATE -> _itemBuilder.pubDate = dateFormatter.parse(trimmedText)
        }
    }

    override fun handleEndTag(tag: String) {
        super.handleEndTag(tag)
        when (tag) {
            TAG_ITEM -> buildResult()
            TAG_CATEGORY -> _itemBuilder.addCategory(_category!!)
            TAG_DESCRIPTION -> _itemBuilder.description
        }
    }

    override fun buildResult() {
        super.buildResult()
        _resultSetter(_itemBuilder.build())
    }
}