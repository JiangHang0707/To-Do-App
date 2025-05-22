package com.example.todoapp.converters

import androidx.room.TypeConverter
import java.util.Date
import kotlin.time.TimedValue


class TypeConverter {

    @TypeConverter
    fun fromTimeStamp(value:Long):Date{
        return Date(value)
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date):Long{
        return date.time
    }
}