// File: MainActivity.kt
package com.neha.mytasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.neha.mytasks.navigation.AppNavGraph
import com.neha.mytasks.ui.theme.MyTasksTheme
import com.neha.mytasks.viewmodel.TaskViewModel
import com.neha.mytasks.auth.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setContent {
            MyTasksTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}
