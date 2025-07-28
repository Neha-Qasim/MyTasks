// --- TaskDatabase.kt (Updated with fallbackToDestructiveMigration)
package com.neha.mytasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.neha.mytasks.model.Task
import com.neha.mytasks.model.Category

@Database(entities = [Task::class, Category::class], version = 9
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                )
                    .fallbackToDestructiveMigration()  // 🔥 Important!
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
