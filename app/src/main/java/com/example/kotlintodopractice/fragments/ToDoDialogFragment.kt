package com.example.kotlintodopractice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.kotlintodopractice.databinding.FragmentToDoDialogBinding
import com.example.kotlintodopractice.utils.model.ToDoData
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
        fun newInstance(taskId: String, name: String, status: String) =
            ToDoDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("taskId", taskId)
                    putString("name", name)
                    putString("status" ,  status)

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
                bundle.getString("taskId").toString(),
                bundle.getString("name").toString(),
                bundle.getString("status").toString(),
            )
        }

        // กำหนดค่าใน EditText ด้วยชื่องาน
        binding.todoEt.setText(toDoData?.name)

        binding.todoClose.setOnClickListener {
            dismiss()
        }

        binding.todoNextBtn.setOnClickListener {
            val name = binding.todoEt.text.toString()
            val status = "newStatus" // ตั้งค่า status ตามที่ต้องการ

            if (name.isNotEmpty()){
                // ตรวจสอบว่า toDoData เป็น null หรือไม่
                if (toDoData == null){
                    // สร้างงานใหม่
                    listener?.saveTask(name, status, binding.todoEt)
                }else{
                    // อัพเดทงานที่มีอยู่
                    toDoData?.apply {
                        this.name = name
                        this.status = status
                    }
                    listener?.updateTask(toDoData!!, binding.todoEt)
                }
            }
        }
    }


    interface OnDialogNextBtnClickListener{
        fun saveTask(todoTask: String, todoEdit: String, todoEt: TextInputEditText)
        fun updateTask(toDoData: ToDoData , todoEdit:TextInputEditText)
    }

}