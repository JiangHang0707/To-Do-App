package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.models.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.utils.Resources

class TaskViewModel(application: Application): AndroidViewModel(application) {

     private val taskRepository = TaskRepository(application)

     fun getTaskList() = taskRepository.getTaskList()

     fun insertTask(task : Task): MutableLiveData<Resources<Long>> {
         return taskRepository.insertTask(task)
     }

     fun deleteTask(task: Task) : MutableLiveData<Resources<Int>> {
         return taskRepository.deleteTask(task)
     }

     fun deleteTaskUsingId(taskId: String) : MutableLiveData<Resources<Int>> {
         return taskRepository.deleteTaskUsingId(taskId)
     }

     fun updateTask(task: Task) : MutableLiveData<Resources<Int>> {
         return taskRepository.updateTask(task)
     }

     fun updateTaskParticularField(taskId:String,title:String,description:String) : MutableLiveData<Resources<Int>> {
         return taskRepository.updateTaskParticularField(taskId, title, description)
         }
     }

