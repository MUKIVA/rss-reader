package com.mukiva.rssreader.addrss.parsing.handlers

interface ParseXmlHandler {
    fun handleStartTag(tag: String)
    fun handleText(text: String, peekTag: String)
    fun handleEndTag(tag: String)
}