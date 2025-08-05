package com.neha.mytasks.data

import com.google.firebase.database.FirebaseDatabase
import com.neha.mytasks.firebase.FirebaseUtils
import com.neha.mytasks.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose


class CategoryRepository {

    private val db = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseUtils.getCurrentUserId() ?: "anonymous"

    fun getAllCategories(): Flow<List<String>> = callbackFlow {
        val categoryRef = db.child("users").child(userId).child("categories")

        val listener = categoryRef.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val categories = snapshot.children.mapNotNull { it.getValue(Category::class.java)?.name }
                trySend(categories)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
        })

        awaitClose { categoryRef.removeEventListener(listener) }
    }

    suspend fun insertCategory(category: Category) {
        val key = db.child("users").child(userId).child("categories").push().key ?: return
        db.child("users").child(userId).child("categories").child(key).setValue(category).await()
    }

    suspend fun deleteCategoryByName(name: String) {
        val categoryRef = db.child("users").child(userId).child("categories")
        categoryRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { child ->
                val item = child.getValue(Category::class.java)
                if (item?.name == name) {
                    child.ref.removeValue()
                }
            }
        }.await()
    }
}
