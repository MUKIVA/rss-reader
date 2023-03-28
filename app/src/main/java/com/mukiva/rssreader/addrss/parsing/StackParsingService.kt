package com.mukiva.rssreader.addrss.parsing

import com.mukiva.rssreader.addrss.parsing.handlers.ParseXmlHandler

interface StackParsingService {
    fun addHandler(handler: ParseXmlHandler)
    fun popHandler(): ParseXmlHandler
    fun addTag(tag: String)
    fun popTag(): String
    fun getAttr(namespace: String, attr: String): String?
}