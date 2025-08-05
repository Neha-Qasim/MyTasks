package com.neha.mytasks.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.neha.mytasks.firebase.FirebaseUtils
import com.neha.mytasks.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance().reference

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        fetchTasks()
        fetchCategories()
    }

    private fun fetchTasks() {
        val uid = FirebaseUtils.getCurrentUserId() ?: return
        db.child("users").child(uid).child("tasks").get()
            .addOnSuccessListener { snapshot ->
                val taskList = snapshot.children.mapNotNull { child ->
                    val task = child.getValue(Task::class.java)
                    task?.copy(key = child.key ?: "")
                }
                _tasks.value = taskList
            }
    }

    private fun fetchCategories() {
        val uid = FirebaseUtils.getCurrentUserId() ?: return
        db.child("users").child(uid).child("categories").get()
            .addOnSuccessListener { snapshot ->
                val categoryList = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                _categories.value = categoryList
            }
    }

    fun getTasksForCategory(category: String): List<Task> =
        _tasks.value.filter { it.category == category }

    fun getTaskByKey(key: String): Task? =
        _tasks.value.find { it.key == key }

    fun addTask(category: String, title: String, description: String, priority: String) {
        val uid = FirebaseUtils.getCurrentUserId() ?: return
        val newRef = db.child("users").child(uid).child("tasks").push()
        val task = Task(
            key = newRef.key ?: "",
            title = title,
            description = description,
            category = category,
            priority = priority.uppercase(),
            isCompleted = false
        )
        newRef.setValue(task).addOnSuccessListener { fetchTasks() }
    }

    fun updateTask(task: Task) {
        val uid = FirebaseUtils.getCurrentUserId() ?: return

        //  Optimistically update UI
        _tasks.value = _tasks.value.map {
            if (it.key == task.key) task else it
        }

        // Sync with Firebase (no need to re-fetch)
        db.child("users").child(uid).child("tasks").child(task.key).setValue(task)
    }
    fun deleteTask(task: Task) {
        val uid = FirebaseUtils.getCurrentUserId() ?: return
        db.child("users").child(uid).child("tasks").child(task.key).removeValue()
            .addOnSuccessListener { fetchTasks() }
    }

    fun markTaskCompleted(task: Task, completed: Boolean) {
        val uid = FirebaseUtils.getCurrentUserId() ?: return
        val updatedTask = task.copy(isCompleted = completed)

        //  Optimistically update local list (instant UI update)
        _tasks.value = _tasks.value.map {
            if (it.key == task.key) updatedTask else it
        }

        // Persist to Firebase
        db.child("users").child(uid).child("tasks").child(task.key).setValue(updatedTask)
    }



    fun addCategory(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return
        val uid = FirebaseUtils.getCurrentUserId() ?: return
        db.child("users").child(uid).child("categories").child(trimmed).setValue(trimmed)
            .addOnSuccessListener { fetchCategories() }
    }

    fun deleteCategory(category: String) {
        val uid = FirebaseUtils.getCurrentUserId() ?: return
        db.child("users").child(uid).child("categories").child(category).removeValue()
            .addOnSuccessListener { fetchCategories() }

        db.child("users").child(uid).child("tasks").get()
            .addOnSuccessListener { snapshot ->
                snapshot.children.forEach { taskSnap ->
                    val task = taskSnap.getValue(Task::class.java)
                    if (task?.category == category) {
                        db.child("users").child(uid).child("tasks").child(taskSnap.key!!).removeValue()
                    }
                }
                fetchTasks()
            }
    }
}
