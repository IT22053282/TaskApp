package com.example.todoapp

import TaskDatabaseHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todoapp.databinding.ActivityAddTaskBinding

class addTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TaskDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TaskDatabaseHelper(this)

        binding.savebtn.setOnClickListener {
            val title = binding.TitleEditText.text.toString()
            val content = binding.descriptioneditText.text.toString()
            val date = binding.dateEditText.text.toString()
            val time = binding.timeEditText.text.toString()

            if (title.isNotBlank() && content.isNotBlank() && date.isNotBlank() && time.isNotBlank()) {
                val task = Task(0, title, content, date, time)
                db.insertTask(task)
                Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }
}