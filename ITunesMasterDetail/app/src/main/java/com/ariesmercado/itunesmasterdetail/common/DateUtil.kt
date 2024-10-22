package com.ariesmercado.itunesmasterdetail.common

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy hh:mma", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun formatDate(dateString: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val targetFormat = SimpleDateFormat("", Locale.getDefault())
        val date = originalFormat.parse(dateString)
        return targetFormat.format(date)
    }
}