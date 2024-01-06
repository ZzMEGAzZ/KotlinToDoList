package com.example.kotlintodopractice.utils.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodopractice.databinding.EachTodoItemBinding
import com.example.kotlintodopractice.utils.ToDoData

class TaskAdapter(private val list: MutableList<ToDoData>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private  val TAG = "TaskAdapter"
    private var listener:TaskAdapterInterface? = null
    fun setListener(listener:TaskAdapterInterface){
        this.listener = listener
    }
    class TaskViewHolder(val binding: EachTodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            EachTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = list[position]
        with(holder.binding) {
            // ตั้งค่าข้อความของงาน - สมมติว่า 'name' เป็นชื่อของงาน
            todoTask.text = currentItem.name

            // Log สำหรับ Debugging
            Log.d(TAG, "onBindViewHolder: $currentItem")

            // ตั้ง Listener สำหรับปุ่ม Edit
            editTask.setOnClickListener {
                listener?.onEditItemClicked(currentItem, position)
            }

            // ตั้ง Listener สำหรับปุ่ม Delete
            deleteTask.setOnClickListener {
                listener?.onDeleteItemClicked(currentItem, position)
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface TaskAdapterInterface{
        fun onDeleteItemClicked(toDoData: ToDoData , position : Int)
        fun onEditItemClicked(toDoData: ToDoData , position: Int)
    }
}