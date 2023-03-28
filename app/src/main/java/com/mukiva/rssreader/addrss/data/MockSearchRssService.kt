package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.addrss.parsing.RssParsingService
import com.mukiva.rssreader.watchfeeds.domain.Feed

class MockSearchRssService : SearchRssService {
    override suspend fun search(link: String): Feed {

        val input = """<?xml version="1.0" encoding="UTF-8" ?>
        <rss version="2.0">
        <channel>
          <title>W3Schools Home Page</title>
          <link>https://www.w3schools.com</link>
          <description>Free web building tutorials</description>
          <item>
            <title>XML Tutorial</title>
            <link>https://www.w3schools.com/xml</link>
            <description>
                <![CDATA[Бразильская супермодель Жизель Бюндхен снялась для рекламы французского бренда Louis Vuitton. Бывший «ангел» Victoria's Secret показала стройную фигуру, позируя на пляже в закрытом черном купальнике указанной марки. 42-летняя манекенщица распустила волосы и дополнила образ массивной золотистой цепочкой.]]>
            </description>
          </item>
        </channel>
        </rss>""".trimIndent()

        val service = RssParsingService()
        service.parse(input.byteInputStream())

        return Feed(
            title = "New feed",
            description = "Some desc",
            newsRepoLink = link,
            imageLink = "",
            news = mutableListOf()
        )
    }
}