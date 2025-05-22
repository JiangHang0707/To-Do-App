package com.example.todoapp.database

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.TypeConverters
import com.example.todoapp.models.Task
import com.example.todoapp.dao.TaskDao
import com.example.todoapp.converters.TypeConverter

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(TypeConverter::class)
abstract class TaskDatabase: RoomDatabase() {

    abstract val taskDao : TaskDao


    companion object{
        @Volatile
        private var INSTANCE : TaskDatabase? = null
        fun getInstance(context: Context) : TaskDatabase{
            synchronized(this,){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "taskdb"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}