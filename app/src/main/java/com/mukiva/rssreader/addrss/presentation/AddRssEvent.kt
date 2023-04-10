package com.mukiva.rssreader.addrss.presentation

sealed class AddRssEvent {
    object AddRssEnd : AddRssEvent()
    data class SendToast(val id : Int) : AddRssEvent()
}