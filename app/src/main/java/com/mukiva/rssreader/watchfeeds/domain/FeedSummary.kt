package com.mukiva.rssreader.watchfeeds.domain

import android.os.Parcel
import android.os.Parcelable

data class FeedSummary(
    val id: Long,
    val title: String,
    val description: String,
    val newsRepoLink: String,
    val imageLink: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(newsRepoLink)
        parcel.writeString(imageLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeedSummary> {
        override fun createFromParcel(parcel: Parcel): FeedSummary {
            return FeedSummary(parcel)
        }

        override fun newArray(size: Int): Array<FeedSummary?> {
            return arrayOfNulls(size)
        }
    }
}