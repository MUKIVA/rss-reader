package com.mukiva.data.mapper.handlers

interface ParseXmlHandler {
    fun handleStartTag(tag: String)
    fun handleText(text: String, peekTag: String)
    fun handleEndTag(tag: String)
}