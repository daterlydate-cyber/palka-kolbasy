package com.phantomclone.data.database

import androidx.room.TypeConverter

/**
 * Room TypeConverters for complex types stored as primitive columns.
 * Strings are stored directly (JSON serialization handled at repository level).
 */
class Converters {
    @TypeConverter
    fun fromLongNullable(value: Long?): Long = value ?: -1L

    @TypeConverter
    fun toLongNullable(value: Long): Long? = if (value == -1L) null else value
}
