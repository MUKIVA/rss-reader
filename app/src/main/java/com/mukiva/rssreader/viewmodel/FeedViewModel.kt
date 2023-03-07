package com.mukiva.rssreader.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedViewModel : ViewModel() {

    private val _contentText: MutableLiveData<String> = MutableLiveData<String>("Feed content")

    val contentText = _contentText

    fun updateContentText(text: String) {
        _contentText.value = text
    }
}