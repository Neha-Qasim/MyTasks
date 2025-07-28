// --- data/CategoryDao.kt
package com.neha.mytasks.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neha.mytasks.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT name FROM category")
    fun getAllCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Query("DELETE FROM category WHERE name = :name")
    suspend fun deleteCategoryByName(name: String)
}
