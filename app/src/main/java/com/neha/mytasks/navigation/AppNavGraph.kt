package com.neha.mytasks.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.neha.mytasks.viewmodel.TaskViewModel
import com.neha.mytasks.Screens.*

@Composable
fun AppNavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {

        // ✅ Splash screen shown for 3 seconds
        composable("splash") {
            SplashScreen(navController)
        }

        // ✅ Category screen with add category option
        composable("categories") {
            CategoryScreen(
                navController = navController,
                viewModel = taskViewModel
            )
        }

        // ✅ Show task list for selected category
        composable("tasks/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            TaskListScreen(
                category = category,
                tasks = taskViewModel.getTasksForCategory(category),
                onBackClick = { navController.popBackStack() },
                onTaskClick = { task ->
                    navController.navigate("task_detail/${task.id}")
                },
                onCheckComplete = { task, completed ->
                    taskViewModel.markTaskCompleted(task, completed)
                },
                onAddClick = {
                    navController.navigate("add_task/$category")
                }
            )
        }

        // ✅ Add new task to the selected category
        composable("add_task/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            AddTaskScreen(
                category = category,
                onAddClick = { title, description, priority ->
                    taskViewModel.addTask(category, title, description, priority) // uppercase handled inside ViewModel
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // ✅ Show detail of selected task with edit/delete
        composable("task_detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: -1
            taskViewModel.getTaskById(taskId)?.let { task ->
                TaskDetailScreen(
                    task = task,
                    onBack = { navController.popBackStack() },
                    onEdit = { editedTask ->
                        taskViewModel.updateTask(editedTask)
                    },
                    onDelete = { toDelete ->
                        taskViewModel.deleteTask(toDelete)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
