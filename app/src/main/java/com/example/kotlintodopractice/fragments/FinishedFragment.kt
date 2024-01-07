package com.example.kotlintodopractice.fragments

<<<<<<< Updated upstream

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodopractice.R
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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kotlintodopractice.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment(){

    private lateinit var binding: FragmentFinishedBinding
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
        init()
    }

    private fun init() {
        // Your initialization code...

        // Example of using NavController to navigate
        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.action_fragment_finished_to_homeFragment)
        }

        // More initialization code...
    }


}

=======
import androidx.fragment.app.Fragment

class FinishedFragment : Fragment(){
}
>>>>>>> Stashed changes
