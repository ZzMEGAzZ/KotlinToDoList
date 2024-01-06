package com.example.kotlintodopractice.utils.data.api

import com.example.kotlintodopractice.utils.data.model.Task
import com.example.kotlintodopractice.utils.data.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.serialization.kotlinx.json.json

object Api {
    private const val BASE_URL = "https://a4dc-171-6-2-231.ngrok-free.app"

    private val apiClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun login(user: String, Password: String): User {
        val url = "$BASE_URL/login/"
        return apiClient.post(url).body() as User
    }

    suspend fun getTasks(): List<Task> {
        val url = "$BASE_URL/"
        return apiClient.get(url).body() as List<Task>
    }

    suspend fun getUsers(): List<User> {
        val url = "$BASE_URL/"
        return apiClient.get(url).body() as List<User>
    }

    suspend fun postTask(task: Task, user_id: String): Task {
        val url = "$BASE_URL/addtask/${user_id}/"
        return apiClient.post(url).body() as Task
    }

    suspend fun postUser(user: User): User {
        val url = "$BASE_URL/adduser/"
        return apiClient.post(url).body() as User
    }

    suspend fun putTask(task: Task): Task {
        val url = "$BASE_URL/"
        return apiClient.put(url).body() as Task
    }

    suspend fun deleteTask(task: Task): Task {
        val url = "$BASE_URL/"
        return apiClient.delete(url).body() as Task
    }

}