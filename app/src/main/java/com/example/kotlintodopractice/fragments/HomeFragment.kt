package com.example.kotlintodopractice.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodopractice.databinding.FragmentHomeBinding
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


class HomeFragment : Fragment(), ToDoDialogFragment.OnDialogNextBtnClickListener,
    TaskAdapter.TaskAdapterInterface {

    private val TAG = "HomeFragment"
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private var frag: ToDoDialogFragment? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var toDoItemList: MutableList<ToDoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        //get data from firebase
        getTaskFromFirebase()

        binding.addTaskBtn.setOnClickListener {

            if (frag != null)
                childFragmentManager.beginTransaction().remove(frag!!).commit()
            frag = ToDoDialogFragment()
            frag!!.setListener(this)

            frag!!.show(
                childFragmentManager,
                ToDoDialogFragment.TAG
            )

        }
    }

    private fun getTaskFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                toDoItemList.clear()
                for (taskSnapshot in snapshot.children) {
                    val name = taskSnapshot.child("name").getValue(String::class.java)

                    if (name != null) {
                        val todoTask = ToDoData(taskSnapshot.key!!, name)
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

    private fun init() {

        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = Firebase.database.reference.child("Tasks")
            .child(authId)


        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)

        toDoItemList = mutableListOf()
        taskAdapter = TaskAdapter(toDoItemList)
        taskAdapter.setListener(this)
        binding.mainRecyclerView.adapter = taskAdapter
    }

    override fun saveTask(name: String, status: String, todoEt: TextInputEditText) {
        // อ่านจำนวนของงานใน Firebase เพื่อหา index ถัดไป
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // สร้าง HashMap สำหรับข้อมูลที่จะเซ็ต
                val taskMap = hashMapOf(
                    "name" to name,
                    "status" to status,
                )

                // เซ็ตข้อมูลงานใหม่ลงใน Firebase
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
        // สร้าง HashMap สำหรับข้อมูลที่จะอัปเดต
        val taskMap = hashMapOf<String, Any>(
            "name" to toDoData.name,
        )

        // อัปเดตงานใน Firebase โดยใช้ taskId และ HashMap ที่เตรียมไว้
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

        // สร้าง HashMap สำหรับข้อมูลที่จะอัปเดต
        val taskMap = hashMapOf<String, Any>(

            "status" to if (isChecked) "done" else "todo"
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
        // ตรวจสอบว่ามี dialog ที่กำลังแสดงอยู่หรือไม่ ถ้ามี ก็จะลบมันออก
        frag?.let {
            childFragmentManager.beginTransaction().remove(it).commit()
        }

        // สร้าง instance ใหม่ของ ToDoDialogFragment ด้วยข้อมูลที่มีอยู่
        frag = ToDoDialogFragment.newInstance(toDoData.taskId, toDoData.name, toDoData.status)
        frag?.setListener(this)
        frag?.show(
            childFragmentManager,
            ToDoDialogFragment.TAG
        )
    }

//    override fun onClickFAB() {
//        binding.doneTaskBtn.setOnClickListener(View.OnClickListener {
//            // navigate to fragment_finished
//            val fragment = FinishedFragment()
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_container, fragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//        })
//        }
//
//    }


}