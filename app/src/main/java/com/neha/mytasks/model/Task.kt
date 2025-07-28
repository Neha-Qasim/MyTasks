package com.neha.mytasks.model



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val priority: String, // High, Medium, Low
    val category: String,
    val isCompleted: Boolean = false
)