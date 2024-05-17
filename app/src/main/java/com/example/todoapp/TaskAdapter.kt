package com.example.todoapp

import TaskDatabaseHelper
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var tasks: List<Task>, private val context: Context) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private val db: TaskDatabaseHelper = TaskDatabaseHelper(context)

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val updateBtn: ImageView = itemView.findViewById(R.id.updateBtn)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]
        holder.titleTextView.text = currentTask.title
        holder.contentTextView.text = currentTask.description
        holder.dateTextView.text = currentTask.date
        holder.timeTextView.text = currentTask.time
        holder.updateBtn.setOnClickListener {
            val intent =
                Intent(holder.itemView.context, updateTaskActivity::class.java).apply {
                    putExtra("task_id", currentTask.id)
                }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            showConfirmationDialog(currentTask.id)
        }
    }

    private fun showConfirmationDialog(taskId: Int) {
        AlertDialog.Builder(context)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete") { dialogInterface: DialogInterface, _: Int ->
                db.deleteTask(taskId)
                refreshData(db.getAllTasks())
                Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    fun refreshData(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
