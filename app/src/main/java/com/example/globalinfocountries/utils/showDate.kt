package com.example.globalinfocountries.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun showDate(publishedAt: String): String {
    val parser = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXXX", Locale.US)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.US)
    val dateTime = LocalDateTime.parse(publishedAt, parser)
    return dateTime.format(formatter)
}