package com.example.kotlintodopractice.utils.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("username")
    val email: String?,
    @SerialName("userid")
    val userid: String?,
    @SerialName("password")
    val password: String?
)