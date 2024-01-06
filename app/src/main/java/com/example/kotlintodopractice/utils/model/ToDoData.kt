package com.example.kotlintodopractice.utils.model

data class ToDoData(
    val taskId: String = "",
    var name: String = "",
    var status: String = "",
    var index: Int = 0
)