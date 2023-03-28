package com.mukiva.rssreader.addrss.parsing

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

abstract class XmlParsingService<TParse> : ParsingService<TParse> {

    protected val parser: XmlPullParser = XmlPullParserFactory.newInstance().newPullParser()

}