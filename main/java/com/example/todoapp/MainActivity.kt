package com.example.todoapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.compose.material3.Snackbar
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.adapter.TaskRVViewBindingAdapter
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.models.Task
import com.example.todoapp.utils.Status
import com.example.todoapp.utils.clearEditText
import com.example.todoapp.utils.longToastShow
import com.example.todoapp.utils.setupDialog
import com.example.todoapp.utils.validateEditText
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.SimpleTimeZone
import java.util.UUID


class MainActivity : AppCompatActivity() {

    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val addTaskDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.add_task_dialog)
        }
    }

    private val updateTaskDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.update_task_dialog)
        }
    }

    private val loadingDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }

    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        // add task start
        val addCloseImg = addTaskDialog.findViewById<ImageView>(R.id.closeImg)
        addCloseImg.setOnClickListener { addTaskDialog.dismiss() }

        val addETTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val addETTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)
        addETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETTitle, addETTitleL)
            }

        })

        val addETDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val addETDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)
        addETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETDesc, addETDescL)
            }

        })

        //date picker
        val datePickerBtn = addTaskDialog.findViewById<Button>(R.id.datePickerBtn)

        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build()

        datePickerBtn.setOnClickListener {
            materialDatePicker.show(supportFragmentManager,"materialDatePicker")
        }

        //timePicker
        val timePickerBtn = addTaskDialog.findViewById<Button>(R.id.timePickerBtn)

        val materialTimePicker = MaterialTimePicker.Builder()
            .setTitleText("Select Time")
            .build()


        timePickerBtn.setOnClickListener {
            materialTimePicker.show(supportFragmentManager,"materialTimePicker")
        }



        mainBinding.addTaskBtn.setOnClickListener {
            clearEditText(addETTitle, addETTitleL)
            clearEditText(addETDesc, addETDescL)
            addTaskDialog.show()
        }

        val saveTaskButton = addTaskDialog.findViewById<Button>(R.id.saveTaskButton)
        saveTaskButton.setOnClickListener {
            if (validateEditText(addETTitle, addETTitleL)
                && validateEditText(addETDesc, addETDescL)
            ) {
                addTaskDialog.dismiss()
                val hour = materialTimePicker.hour
                val minute = materialTimePicker.minute
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
                    materialDatePicker.selection!!,
                    calendar.timeInMillis
                )

                taskViewModel.insertTask(newTask).observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            loadingDialog.show()
                        }

                        Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            if (it.data?.toInt() != -1) {
                                longToastShow("Task added Successfully!")
                            }

                        }

                        Status.ERROR -> {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }
                    }
                }
            }
        }
        //add task end

        // update task start
        val updateETTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val updateETTitleL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        updateETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETTitle, updateETTitleL)
            }
        })

        val updateETDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val updateETDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        updateETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETDesc, updateETDescL)
            }

        })

        val updateCloseImg = updateTaskDialog.findViewById<ImageView>(R.id.closeImg)
        updateCloseImg.setOnClickListener { updateTaskDialog.dismiss() }

        val updateTaskBtn = updateTaskDialog.findViewById<Button>(R.id.updateTaskBtn)
        val updateTimePickerBtn = updateTaskDialog.findViewById<Button>(R.id.timePickerBtn)
        val updateDatePickerBtn = updateTaskDialog.findViewById<Button>(R.id.datePickerBtn)
    // update task end

    val taskRecyclerViewAdapter = TaskRVViewBindingAdapter { type, position, task ->
        if (type == "delete") {
            taskViewModel
                .deleteTaskUsingId(task.id)


                .observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            loadingDialog.show()
                        }

                        Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            if (it.data != -1) {
                                longToastShow("Task Deleted Successfully")
                            }
                        }

                        Status.ERROR -> {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }
                    }
                }
        } else if (type == "update") {
            updateETTitle.setText(task.title)
            updateETDesc.setText(task.description)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = task.time
            val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
            val initialMinute = calendar.get(Calendar.MINUTE)

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(initialHour)
                .setMinute(initialMinute)
                .setTitleText("Select Time")
                .build()
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(task.date)
                .build()
            updateDatePickerBtn.setOnClickListener {
                datePicker.show(supportFragmentManager,"materialDatePicker")
            }

            updateTimePickerBtn.setOnClickListener {
                timePicker.show(supportFragmentManager,"materialTimePicker")
            }

            updateTaskBtn.setOnClickListener {
                if (validateEditText(updateETTitle, updateETTitleL)
                    && validateEditText(updateETDesc, updateETDescL)
                ) {
                    val updatedTime = if (timePicker.hour != null && timePicker.minute != null) {
                        val hour = timePicker.hour
                        val minute = timePicker.minute
                        calendar.apply {
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                        }.timeInMillis
                    }else{
                        task.time
                    }
                    val updateTask = Task (
                        task.id,
                        updateETTitle.text.toString().trim(),
                        updateETDesc.text.toString().trim(),
                        datePicker.selection!!,
                        updatedTime
                    )
                    updateTaskDialog.dismiss()
                    loadingDialog.show()
                    taskViewModel
                        .updateTask(updateTask)
                        .observe(this) {
                            when (it.status) {
                                Status.LOADING -> {
                                    loadingDialog.show()
                                }

                                Status.SUCCESS -> {
                                    loadingDialog.dismiss()
                                    if (it.data != -1) {
                                        longToastShow("Task Updated Successfully")
                                    }
                                }

                                Status.ERROR -> {
                                    loadingDialog.dismiss()
                                    it.message?.let { it1 -> longToastShow(it1) }
                                }
                            }
                        }
                }
            }
            updateTaskDialog.show()
        }
    }
    mainBinding.taskRV.adapter = taskRecyclerViewAdapter
    callGetTaskList(taskRecyclerViewAdapter)
}

    private fun callGetTaskList(taskRecyclerViewAdapter: TaskRVViewBindingAdapter){
            loadingDialog.show()
            CoroutineScope(Dispatchers.Main).launch {
            taskViewModel.getTaskList().collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Status.SUCCESS -> {
                        loadingDialog.dismiss()
                        it.data?.collect{taskList ->
                            loadingDialog.dismiss()
                            taskRecyclerViewAdapter.addAllTask(taskList)
                        }

                    }
                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }
                    }
                }
            }
        }

        val switch = findViewById<SwitchCompat>(R.id.switch1)

        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night",false)

        if (nightMode){
            switch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        switch.setOnCheckedChangeListener { buttonView, isChecked ->

            if (!isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()

            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night",true)
                editor.apply()
            }
        }
    }
}
