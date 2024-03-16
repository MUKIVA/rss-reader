package com.mukiva.core.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    fun formatAsDayOfYear(date: Date): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

}