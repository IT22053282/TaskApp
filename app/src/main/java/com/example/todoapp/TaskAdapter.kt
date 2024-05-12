package com.example.todoapp

import TaskDatabaseHelper
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter (private var tasks :List<Task>,context: Context):
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
        private val db: TaskDatabaseHelper = TaskDatabaseHelper(context)

    class TaskViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView:TextView = itemView.findViewById(R.id.contentTextView)
        val updateBtn:ImageView = itemView.findViewById(R.id.updateBtn)
        val deleteButton:ImageView = itemView.findViewById(R.id.deleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item,parent,false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]
        holder.titleTextView.text = currentTask.title
        holder.contentTextView.text = currentTask.description
        holder.updateBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, updateTaskActivity::class.java).apply {
                putExtra("task_id", currentTask.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteTask(currentTask.id)
            refreshData(db.getAllTasks())
            Toast.makeText(holder.itemView.context, "Task Deleted", Toast.LENGTH_SHORT).show()
        }

//        Log.d("TaskAdapter", "Bound task at position $position")
    }

    fun refreshData(newTasks:List<Task>){
        tasks = newTasks
        notifyDataSetChanged()

    }
}