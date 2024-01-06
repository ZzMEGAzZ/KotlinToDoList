package com.example.kotlintodopractice.utils.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("taskId")
    val taskId: String?,
    @SerialName("userid")
    val userid: String?,
    @SerialName("status")
    val status: String?,
    @SerialName("index")
    val index: String??
)
