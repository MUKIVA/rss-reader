package com.mukiva.rssreader.addrss.data.parsing

import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.addrss.data.parsing.handlers.ParseXmlHandler
import com.mukiva.rssreader.addrss.data.parsing.handlers.RssParseHandler
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.util.Stack

class RssParser : XmlParer<Rss>(), StackParser {

    private val _tagStack = Stack<String>()
    private val _handlerStack = Stack<ParseXmlHandler>()

    private var _rss: Rss? = null

    override fun parse(stream: InputStream): Rss {
        _tagStack.clear()
        _handlerStack.clear()
        _rss = null
        _handlerStack.add(RssParseHandler(this) {
            _rss = it
        })
        parser.setInput(stream, "UTF-8")
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {

            when (parser.eventType) {
                XmlPullParser.START_TAG ->
                    _handlerStack.peek().handleStartTag(parser.name)
                XmlPullParser.TEXT ->
                    _handlerStack.peek().handleText(parser.text, _tagStack.peek())
                XmlPullParser.END_TAG ->
                    _handlerStack.peek().handleEndTag(parser.name)
                XmlPullParser.CDSECT ->
                    _handlerStack.peek().handleText(parser.text, _tagStack.peek())
                else -> {}
            }

            parser.nextToken()
        }
        return _rss ?: throw IllegalStateException()
    }

    override fun addHandler(handler: ParseXmlHandler) {
        _handlerStack.add(handler)
    }

    override fun popHandler(): ParseXmlHandler {
        return _handlerStack.pop()
    }

    override fun addTag(tag: String) {
        _tagStack.add(tag)
    }

    override fun popTag(): String {
        return _tagStack.pop()
    }

    override fun getAttr(namespace: String, attr: String): String? {
        return try {
            parser.getAttributeValue(namespace, attr)
        } catch (e: Exception) {
            null
        }

    }
}


