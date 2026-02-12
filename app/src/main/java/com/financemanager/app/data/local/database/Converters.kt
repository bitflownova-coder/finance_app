package com.financemanager.app.data.local.database

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converters for Room database
 * Converts between database types and app types
 */
class Converters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
