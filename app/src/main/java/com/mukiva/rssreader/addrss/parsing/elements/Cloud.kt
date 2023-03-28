package com.mukiva.rssreader.addrss.parsing.elements

data class Cloud(
    val domain: String,
    val port: String,
    val path: String,
    val registerProcedure: String,
    val protocol: String
)
