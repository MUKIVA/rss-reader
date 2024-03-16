package com.mukiva.data.entity

data class Cloud(
    val id: Long = 0,
    val domain: String = "",
    val port: String = "",
    val path: String = "",
    val registerProcedure: String = "",
    val protocol: String = ""
)