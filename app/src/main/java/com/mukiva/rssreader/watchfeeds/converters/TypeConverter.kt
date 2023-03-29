package com.mukiva.rssreader.watchfeeds.converters

interface TypeConverter<TIn, TOut> {
    fun convert(obj: TIn): TOut
}