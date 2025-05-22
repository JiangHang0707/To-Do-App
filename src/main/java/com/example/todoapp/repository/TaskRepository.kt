package com.example.todoapp.repository

import android.app.ActivityManager.TaskDescription
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.dao.TaskDao
import com.example.todoapp.database.TaskDatabase
import com.example.todoapp.models.Task
import com.example.todoapp.utils.Resources
import com.example.todoapp.utils.Resources.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TaskRepository (application : Application) {

    private val taskDao : TaskDao = TaskDatabase.getInstance(application).taskDao

    fun getTaskList() = flow {
        emit(Loading())
        try {
            val result = taskDao.getTaskList()
            emit(Success(result))
        } catch (e: Exception){
            emit(Error(e.message.toString()))
        }
    }

    fun insertTask(task : Task) = MutableLiveData<Resources<Long>>().apply {
        postValue(Loading())
        try{
            CoroutineScope(Dispatchers.IO).launch{
                val result = taskDao.insertTask(task)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun deleteTask(task : Task) = MutableLiveData<Resources<Int>>().apply {
        postValue(Loading())
        try{
            CoroutineScope(Dispatchers.IO).launch{
                val result = taskDao.deleteTask(task)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun deleteTaskUsingId(taskId: String) = MutableLiveData<Resources<Int>>().apply {
         postValue(Loading())
         try {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = taskDao.deleteTaskUsingId(taskId)
                    postValue(Success(result))
                }
            } catch (e: Exception) {
                postValue(Error(e.message.toString()))
            }
        }

    fun updateTask(task : Task ) = MutableLiveData<Resources<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.updateTask(task)
                postValue(Success(result))
            }
        } catch (e: Exception) {
            postValue(Error(e.message.toString()))
        }
    }

    fun updateTaskParticularField(taskId: String,title: String,description: String ) = MutableLiveData<Resources<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.updateTaskPaticularField(taskId, title, description)
                postValue(Success(result))
            }
        } catch (e: Exception) {
            postValue(Error(e.message.toString()))
        }
    }
}
