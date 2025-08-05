package com.neha.mytasks.data

import com.google.firebase.database.FirebaseDatabase
import com.neha.mytasks.firebase.FirebaseUtils
import com.neha.mytasks.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose


class TaskRepository {

    private val db = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseUtils.getCurrentUserId() ?: "anonymous"

    fun getAllTasks(): Flow<List<Task>> = callbackFlow {
        val taskRef = db.child("users").child(userId).child("tasks")

        val listener = taskRef.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val tasks = snapshot.children.mapNotNull { child ->
                    val task = child.getValue(Task::class.java)
                    task?.copy(key = child.key ?: "")
                }
                trySend(tasks)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
        })

        awaitClose { taskRef.removeEventListener(listener) }
    }


    suspend fun insertTask(task: Task) {
        val key = db.child("users").child(userId).child("tasks").push().key ?: return
        val taskWithKey = task.copy(key = key)
        db.child("users").child(userId).child("tasks").child(key).setValue(taskWithKey).await()
    }


    suspend fun updateTask(task: Task) {
        if (task.key.isBlank()) return
        db.child("users").child(userId).child("tasks").child(task.key).setValue(task).await()
    }


    suspend fun deleteTask(task: Task) {
        if (task.key.isBlank()) return
        db.child("users").child(userId).child("tasks").child(task.key).removeValue().await()
    }


    suspend fun deleteTasksByCategory(category: String) {
        val taskRef = db.child("users").child(userId).child("tasks")
        taskRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { child ->
                val item = child.getValue(Task::class.java)
                if (item?.category == category) {
                    child.ref.removeValue()
                }
            }
        }.await()

}
}
