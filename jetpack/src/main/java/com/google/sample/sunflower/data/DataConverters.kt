package com.google.sample.sunflower.data

import androidx.room.TypeConverter
import java.util.*

class DataConverters {

    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply {
            timeInMillis = value
        }
}