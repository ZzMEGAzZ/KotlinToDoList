package com.example.kotlintodopractice.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodopractice.R
import com.example.kotlintodopractice.utils.adapter.TaskAdapter
import com.example.kotlintodopractice.utils.model.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kotlintodopractice.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment(), ToDoDialogFragment.OnDialogNextBtnClickListener,
    TaskAdapter.TaskAdapterInterface {

    private val TAG = "FinishedFragment"
    private lateinit var binding: FragmentFinishedBinding
    private lateinit var database: DatabaseReference
    private var frag: ToDoDialogFragment? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var toDoItemList: MutableList<ToDoData>
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize NavController
        navController = Navigation.findNavController(view)

        // Your other code...
        init(view)
        //get data from firebase
        getTaskFromFirebase()
    }
    private fun getTaskFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toDoItemList.clear()
                for (taskSnapshot in snapshot.children) {
                    val name = taskSnapshot.child("name").getValue(String::class.java)
                    val status = taskSnapshot.child("status").getValue(String::class.java) ?: "default_status"

                    if (name != null && status == "done") {
                        val todoTask = ToDoData(taskSnapshot.key!!, name, status)
                        toDoItemList.add(todoTask)
                    }
                }

                Log.d(TAG, "onDataChange: " + toDoItemList)
                taskAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun init(view: View) {
        // Your initialization code...
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = Firebase.database.reference.child("Tasks")
            .child(authId)
        // Example of using NavController to navigate
        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_fragment_finished_to_homeFragment)
        }
        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        toDoItemList = mutableListOf()
        taskAdapter = TaskAdapter(toDoItemList)
        taskAdapter.setListener(this)
        binding.mainRecyclerView.adapter = taskAdapter
        navController = Navigation.findNavController(view)
    }
    override fun saveTask(name: String, status: String, todoEt: TextInputEditText) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val taskMap = hashMapOf(
                    "name" to name,
                    "status" to status,
                )

                database.push().setValue(taskMap)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                            todoEt.text = null
                        } else {
                            Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
        frag?.dismiss() // ใช้ ? เพื่อป้องกัน NullPointerException
    }
    override fun updateTask(toDoData: ToDoData, todoEdit: TextInputEditText) {
        val taskMap = hashMapOf<String, Any>(
            "name" to toDoData.name,
        )

        database.child(toDoData.taskId).updateChildren(taskMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            frag?.dismiss()
        }
    }

    override fun onCheckBoxClicked(toDoData: ToDoData, isChecked: Boolean, position: Int) {

        val taskMap = hashMapOf<String, Any>(

            "status" to if (isChecked) "done" else "todo"
        )

        database.child(toDoData.taskId).updateChildren(taskMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
                getTaskFromFirebase()
            } else {
                Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            frag?.dismiss()
        }
        //getTaskFromFirebase()
    }


    override fun onDeleteItemClicked(toDoData: ToDoData, position: Int) {
        database.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditItemClicked(toDoData: ToDoData, position: Int) {
        frag?.let {
            childFragmentManager.beginTransaction().remove(it).commit()
        }

        frag = ToDoDialogFragment.newInstance(toDoData.taskId, toDoData.name, toDoData.status)
        frag?.setListener(this)
        frag?.show(
            childFragmentManager,
            ToDoDialogFragment.TAG
        )
    }
}