package com.mukiva.data.mapper

import com.mukiva.data.mapper.handlers.ParseXmlHandler

interface StackParser {
    fun addHandler(handler: ParseXmlHandler)
    fun popHandler(): ParseXmlHandler
    fun addTag(tag: String)
    fun popTag(): String
    fun getAttr(namespace: String, attr: String): String?
}