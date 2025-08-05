package com.neha.mytasks.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.neha.mytasks.Screens.*
import com.neha.mytasks.auth.AuthViewModel
import com.neha.mytasks.auth.SignInScreen
import com.neha.mytasks.auth.SignUpScreen
import com.neha.mytasks.viewmodel.TaskViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("auth_gate") {
            AuthGate(
                navController = navController,
                authViewModel = authViewModel,
                taskViewModel = taskViewModel
            )
        }

        composable("sign_in") {
            SignInScreen(
                navController = navController,
                authViewModel = authViewModel,
                taskViewModel = taskViewModel
            )
        }

        composable("sign_up") {
            SignUpScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("categories") {
            CategoryScreen(
                navController = navController,
                viewModel = taskViewModel,
                authViewModel = authViewModel
            )
        }

        composable("tasks/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""

            TaskListScreen(
                category = category,
                tasks = taskViewModel.getTasksForCategory(category),
                onBackClick = { navController.popBackStack() },
                onTaskClick = { task ->
                    navController.navigate("task_detail/${task.key}")
                },
                onAddClick = {
                    navController.navigate("add_task/$category")
                }
            )
        }

        composable("add_task/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            AddTaskScreen(
                category = category,
                onAddClick = { title, description, priority ->
                    taskViewModel.addTask(category, title, description, priority)
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("task_detail/{taskKey}") { backStackEntry ->
            val taskKey = backStackEntry.arguments?.getString("taskKey") ?: ""
            val tasks by taskViewModel.tasks.collectAsState()

            val selectedTask = tasks.find { it.key == taskKey }

            selectedTask?.let { task ->
                TaskDetailScreen(
                    task = task,
                    onBack = { navController.popBackStack() },
                    onEdit = { editedTask ->
                        taskViewModel.updateTask(editedTask)
                    },
                    onDelete = { deletedTask ->
                        taskViewModel.deleteTask(deletedTask)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
