package com.example.kotlintodopractice.utils

data class ToDoData(
    val taskId: String = "",
    var name: String = "",
    var status: String = "",
    var index: Int = 0,
) {
    companion object {
        val taskId: String = ""
    }

}