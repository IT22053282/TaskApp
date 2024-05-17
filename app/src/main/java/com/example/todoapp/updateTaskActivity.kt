package com.example.todoapp

import TaskDatabaseHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todoapp.databinding.ActivityUpdateBinding

class updateTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: TaskDatabaseHelper
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root) // Updated: Use binding.root

        db = TaskDatabaseHelper(this)
        taskId = intent.getIntExtra("task_id", -1)
        if (taskId == -1) {
            finish()
            return
        }

        val task = db.getTaskById(taskId)
        if (task != null) { // Added null check
            binding.updateTitleEditText.setText(task.title)
            binding.UpdateDescriptionEditText.setText(task.description)
            binding.updateDateEditText.setText(task.date)
            binding.updateTimeEditText.setText(task.time)
        }

        binding.updateSavebtn.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.UpdateDescriptionEditText.text.toString()
            val newDate = binding.updateDateEditText.text.toString() // Get the new date
            val newTime = binding.updateTimeEditText.text.toString()
            val updatedTask = Task(taskId, newTitle, newContent, newDate, newTime)
            db.updateTask(updatedTask)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }
    }
}
