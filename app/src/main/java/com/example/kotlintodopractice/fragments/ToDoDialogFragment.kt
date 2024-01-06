package com.example.kotlintodopractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.kotlintodopractice.databinding.FragmentToDoDialogBinding
import com.example.kotlintodopractice.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText

class ToDoDialogFragment : DialogFragment() {

    private lateinit var binding:FragmentToDoDialogBinding
    private var listener : OnDialogNextBtnClickListener? = null
    private var toDoData: ToDoData? = null


    fun setListener(listener: OnDialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "DialogFragment"
        @JvmStatic
        fun newInstance(taskId: String, task: String, status: String, index: Int) =
            ToDoDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("taskId", taskId)
                    putString("task", task)
                    putString("status" ,  status)
                    putInt("index" ,  index)

                }
            }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentToDoDialogBinding.inflate(inflater , container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ตรวจสอบว่าได้รับ arguments หรือไม่
        toDoData = arguments?.let { bundle ->
            ToDoData(
                bundle.getString("taskId").orEmpty(),
                bundle.getString("name").orEmpty(),
                bundle.getString("status").orEmpty(),
                bundle.getInt("index", 0) // ใช้ getInt และให้ค่า default เป็น 0
            )
        }

        // กำหนดค่าใน EditText ด้วยชื่องาน
        binding.todoEt.setText(toDoData?.name)

        binding.todoClose.setOnClickListener {
            dismiss()
        }

        binding.todoNextBtn.setOnClickListener {
            val name = binding.todoEt.text.toString()
            val status = "todo" // ตั้งค่า status ตามที่ต้องการ
            val index = 0 // ตั้งค่า index เป็น 0 หรือตามค่าที่ได้จากการคำนวณหรือรับค่าจาก UI

            if (name.isNotEmpty()){
                // ตรวจสอบว่า toDoData เป็น null หรือไม่
                if (toDoData == null){
                    // สร้างงานใหม่
                    listener?.saveTask(name, status, index, binding.todoEt)
                }else{
                    // อัพเดทงานที่มีอยู่
                    toDoData?.apply {
                        this.name = name
                        this.status = status
                        this.index = index
                    }
                    listener?.updateTask(toDoData!!, binding.todoEt)
                }
            }
        }
    }


    interface OnDialogNextBtnClickListener{
        fun saveTask(todoTask: String, todoEdit: String, newIndex: Int, todoEt: TextInputEditText)
        fun updateTask(toDoData: ToDoData, todoEdit:TextInputEditText)
    }

}