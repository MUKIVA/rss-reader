package com.mukiva.rssreader.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedItemViewModel : ViewModel() {

    private val _contentText: MutableLiveData<String> = MutableLiveData("Feed item content")
    val contentText: MutableLiveData<String> = _contentText
}