package com.mukiva.data.mapper

import com.mukiva.data.entity.Rss
import com.mukiva.data.mapper.handlers.ParseXmlHandler
import com.mukiva.data.mapper.handlers.RssParseHandler
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.util.Stack

class RssParser : Parser, StackParser {

    private val _tagStack = Stack<String>()
    private val _handlerStack = Stack<ParseXmlHandler>()
    private val _parser: XmlPullParser = XmlPullParserFactory
        .newInstance()
        .newPullParser()

    private var _rss: Rss? = null

    override fun parse(stream: InputStream): Rss {
        _tagStack.clear()
        _handlerStack.clear()
        _rss = null
        _handlerStack.add(RssParseHandler(this) {
            _rss = it
        })
        _parser.setInput(stream, null)
        while (_parser.eventType != XmlPullParser.END_DOCUMENT) {

            when (_parser.eventType) {
                XmlPullParser.START_TAG ->
                    _handlerStack.peek().handleStartTag(_parser.name)
                XmlPullParser.TEXT ->
                    _handlerStack.peek().handleText(_parser.text, _tagStack.peek())
                XmlPullParser.END_TAG ->
                    _handlerStack.peek().handleEndTag(_parser.name)
                XmlPullParser.CDSECT ->
                    _handlerStack.peek().handleText(_parser.text, _tagStack.peek())
                else -> {}
            }

            _parser.nextToken()
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
            _parser.getAttributeValue(namespace, attr)
        } catch (e: Exception) {
            null
        }
    }
}


