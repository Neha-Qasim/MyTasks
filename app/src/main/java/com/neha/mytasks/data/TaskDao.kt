package com.neha.mytasks.data

import androidx.room.*
import com.neha.mytasks.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

    @Query("""
        SELECT * FROM task 
        WHERE category = :category 
        ORDER BY CASE priority 
            WHEN 'HIGH' THEN 1 
            WHEN 'MEDIUM' THEN 2 
            ELSE 3 
        END
    """)
    fun getTasksByCategory(category: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id: Int): Task?

    @Query("DELETE FROM task WHERE category = :category")
    suspend fun deleteTasksByCategory(category: String)

}