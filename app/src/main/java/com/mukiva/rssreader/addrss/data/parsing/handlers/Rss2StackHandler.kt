package com.mukiva.rssreader.addrss.data.parsing.handlers

import com.mukiva.rssreader.addrss.data.parsing.StackParsingService
import java.text.SimpleDateFormat
import java.util.*

abstract class Rss2StackHandler(
    _context: StackParsingService
) : BaseStackParseHandler(_context) {

    protected val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH)

    companion object {
        const val TAG_CHANNEL = "channel"
        const val TAG_TITLE = "title"
        const val TAG_LINK = "link"
        const val TAG_DESCRIPTION = "description"
        const val TAG_LANGUAGE = "language"
        const val TAG_COPYRIGHT = "copyright"
        const val TAG_MANAGING_EDITOR = "managingEditor"
        const val TAG_WEB_MASTER = "webMaster"
        const val TAG_PUB_DATE = "pubDate"
        const val TAG_LAST_BUILD_DATE = "lastBuildDate"
        const val TAG_GENERATOR = "generator"
        const val TAG_DOCS = "docs"
        const val TAG_CLOUD = "cloud"
        const val TAG_TTL = "ttl"
        const val TAG_IMAGE = "image"
        const val TAG_TEXT_INPUT = "textInput"
        const val TAG_SKIP_HOURS = "skipHours"
        const val TAG_SKIP_DAYS = "skipDays"
        const val TAG_CATEGORY = "category"
        const val TAG_RATING = "rating"
        const val TAG_ITEM = "item"
        const val TAG_URL = "url"
        const val TAG_WIDTH = "width"
        const val TAG_HEIGHT = "height"
        const val TAG_COMMENTS = "comments"
        const val TAG_AUTHOR = "author"
        const val TAG_ENCLOSURE = "enclosure"
        const val TAG_GUID = "guid"
        const val TAG_SOURCE = "source"
        const val TAG_RSS = "rss"

        const val ATTR_DOMAIN = "domain"
        const val ATTR_PORT = "port"
        const val ATTR_PATH = "path"
        const val ATTR_REGISTER_PROCEDURE = "registerProcedure"
        const val ATTR_PROTOCOL = "protocol"
        const val ATTR_URL = "url"
        const val ATTR_LENGTH = "length"
        const val ATTR_TYPE = "type"
        const val ATTR_IS_PERMA_LINK = "isPermaLink"
        const val ATTR_VERSION = "version"
    }
}