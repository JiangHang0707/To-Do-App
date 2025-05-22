package com.example.todoapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Task(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "TaskId")
    val id : String,
    @ColumnInfo(name = "TaskTitle")
    val title: String,
    val description: String,
    val date: Long, // Store date as a timestamp
    val time: Long  // Store time as a timestamp
)
