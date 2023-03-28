package com.mukiva.rssreader.addrss.parsing.builders

import com.mukiva.rssreader.addrss.parsing.elements.TextInput

class TextInputBuilder {
    var title: String = ""
    var description: String = ""
    var name: String = ""
    var link: String = ""

    fun build(): TextInput {
        return TextInput(
            title,
            description,
            name,
            link
        )
    }
}