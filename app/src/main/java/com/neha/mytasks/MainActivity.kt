package com.neha.mytasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.neha.mytasks.data.TaskDatabase
import com.neha.mytasks.data.TaskRepository
import com.neha.mytasks.data.CategoryRepository
import com.neha.mytasks.navigation.AppNavGraph
import com.neha.mytasks.ui.theme.MyTasksTheme
import com.neha.mytasks.viewmodel.TaskViewModel
import com.neha.mytasks.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Step 1: Create a singleton database instance
        val database = TaskDatabase.getDatabase(applicationContext)

        // ✅ Step 2: Build repositories
        val taskRepository = TaskRepository(database.taskDao())
        val categoryRepository = CategoryRepository(database.categoryDao())

        // ✅ Step 3: Create ViewModel with custom factory
        val factory = TaskViewModelFactory(taskRepository, categoryRepository)
        val taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        // ✅ Step 4: Set Composable content with theme and navigation graph
        setContent {
            MyTasksTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController, taskViewModel = taskViewModel)
            }
        }
    }
}
