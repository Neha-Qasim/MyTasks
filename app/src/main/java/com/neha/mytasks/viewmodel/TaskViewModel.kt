package com.neha.mytasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.neha.mytasks.data.CategoryRepository
import com.neha.mytasks.data.TaskRepository
import com.neha.mytasks.model.Category
import com.neha.mytasks.model.Task
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepo: TaskRepository,
    private val categoryRepo: CategoryRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    // ✅ Get all categories as a list of strings
    val categories: StateFlow<List<String>> = categoryRepo.getAllCategories().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            taskRepo.getAllTasks().collect { _tasks.value = it }
        }
    }

    // ✅ Grouping for Category Screen (Optional Use)
    fun tasksGroupedByCategory(): Map<String, List<Task>> =
        _tasks.value.groupBy { it.category }

    // ✅ Get Tasks by category
    fun getTasksForCategory(category: String): List<Task> =
        _tasks.value.filter { it.category == category }.sortedBy {
            when (it.priority.uppercase()) {
                "HIGH" -> 1
                "MEDIUM" -> 2
                else -> 3
            }
        }

    // ✅ Fixed addTask function
    fun addTask(category: String, title: String, description: String, priority: String) {
        val task = Task(
            title = title,
            description = description,
            priority = priority.uppercase(),
            category = category
        )
        viewModelScope.launch {
            taskRepo.insertTask(task)
        }
    }

    fun updateTask(task: Task) = viewModelScope.launch { taskRepo.updateTask(task) }

    fun deleteTask(task: Task) = viewModelScope.launch { taskRepo.deleteTask(task) }

    fun getTaskById(id: Int): Task? = _tasks.value.find { it.id == id }

    fun markTaskCompleted(task: Task, completed: Boolean) = viewModelScope.launch {
        taskRepo.updateTask(task.copy(isCompleted = completed))
    }

    fun addCategory(name: String) = viewModelScope.launch {
        categoryRepo.insertCategory(Category(name = name))
    }
    fun deleteCategory(category: String) = viewModelScope.launch {
        categoryRepo.deleteCategoryByName(category)
        taskRepo.deleteTasksByCategory(category)
    }


}

class TaskViewModelFactory(
    private val taskRepo: TaskRepository,
    private val categoryRepo: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(taskRepo, categoryRepo) as T
    }
}
