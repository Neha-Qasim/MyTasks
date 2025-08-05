package com.neha.mytasks.model

data class Task(
    val key: String = "", //Firebase key
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val priority: String = "",
    val isCompleted: Boolean = false
)

