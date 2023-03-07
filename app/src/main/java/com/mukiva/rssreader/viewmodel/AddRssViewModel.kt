package com.mukiva.rssreader.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddRssViewModel : ViewModel() {

    private val _contentText: MutableLiveData<String> = MutableLiveData("Add rss content")
    val contentText: MutableLiveData<String> = _contentText
}