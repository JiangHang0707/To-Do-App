package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ViewTaskLayoutBinding
import com.example.todoapp.models.Task
import com.example.todoapp.utils.formatDate
import com.example.todoapp.utils.formatTime
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRVViewBindingAdapter(
    private val deleteUpdateCallback: (type: String, position : Int, task : Task) -> Unit
):
    RecyclerView.Adapter<TaskRVViewBindingAdapter.ViewHolder>(){

        private val taskList = arrayListOf<Task>()

        class ViewHolder( val viewTaskLayoutBinding: ViewTaskLayoutBinding )
            :RecyclerView.ViewHolder(viewTaskLayoutBinding.root)

    fun addAllTask(newTaskList: List<Task>){
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ViewTaskLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task = taskList[position]

        holder.viewTaskLayoutBinding.TitleText.text = task.title
        holder.viewTaskLayoutBinding.DescribeText.text = task.description

        val formattedDate = formatDate(task.date)
        val formattedTime = formatTime(task.time)

        holder.viewTaskLayoutBinding.DateText.text = formattedDate
        holder.viewTaskLayoutBinding.TimeText.text = formattedTime

        holder.viewTaskLayoutBinding.deleteImg.setOnClickListener {
            if (holder.adapterPosition != -1 ){
                deleteUpdateCallback("delete",holder.adapterPosition,task)
            }
        }

        holder.viewTaskLayoutBinding.editImg.setOnClickListener {
            if (holder.adapterPosition != -1 ){
                deleteUpdateCallback("update",holder.adapterPosition,task)
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}