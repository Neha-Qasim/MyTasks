// --- data/CategoryRepository.kt
package com.neha.mytasks.data

import com.neha.mytasks.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    fun getAllCategories(): Flow<List<String>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    suspend fun deleteCategoryByName(name: String) {
        categoryDao.deleteCategoryByName(name)
    }


}
