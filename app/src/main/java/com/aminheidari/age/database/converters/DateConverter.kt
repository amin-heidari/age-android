package com.aminheidari.age.database.converters

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    companion object {

        @TypeConverter
        @JvmStatic
        fun fromTimestamp(timestamp: Long) = Date(timestamp)

        @TypeConverter
        @JvmStatic
        fun toTimestamp(date: Date) = date.time
    }

}