package com.mukiva.rssreader.addrss.data.parsing

import com.mukiva.rssreader.addrss.data.parsing.handlers.ParseXmlHandler

interface StackParser {
    fun addHandler(handler: ParseXmlHandler)
    fun popHandler(): ParseXmlHandler
    fun addTag(tag: String)
    fun popTag(): String
    fun getAttr(namespace: String, attr: String): String?
}